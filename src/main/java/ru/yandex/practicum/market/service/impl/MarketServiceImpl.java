package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.*;
import ru.yandex.practicum.market.entity.*;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.enums.ItemSort;
import ru.yandex.practicum.market.exception.OrderNotFoundException;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.service.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final ItemService itemService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ItemInCartService itemInCartService;
    private final ItemInOrderService itemInOrderService;
    private final ItemMapper itemMapper;

    @Override
    public Mono<ItemsPageDto> getItems(String search, ItemSort itemSort, int pageNumber, int pageSize) {
        Mono<SearchResult> items = itemService.search(search, itemSort, pageNumber, pageSize);
        Mono<CartDto> cart = getActiveCartDto();
        return Mono.zip(items, cart)
                .map(tuple -> {

                    SearchResult result = tuple.getT1();

                    CartDto cartDto = tuple.getT2();

                    Map<Long, Integer> counts = cartDto.items().stream()
                            .collect(Collectors.toMap(
                                    ItemDto::itemId,
                                    ItemDto::count));

                    List<ItemDto> itemsDto = result.items().stream()
                            .map(item -> new ItemDto(
                                    item.getItemId(),
                                    item.getTitle(),
                                    item.getDescription(),
                                    item.getImgPath(),
                                    item.getPrice(),
                                    counts.getOrDefault(item.getItemId(), 0)
                            ))
                            .toList();

                    return new ItemsPageDto(
                            splitByThree(itemsDto),
                            new PagingDto(
                                    pageNumber,
                                    pageSize,
                                    pageNumber > 1,
                                    (long) pageSize * pageNumber < result.total()
                            ));
                });

    }

    @Override
    public Mono<ItemDto> getItemDtoById(Long itemId) {
        return getItemDto(itemId);
    }

    @Override
    public Mono<Integer> getItemInCartCount(Long itemId) {
        Mono<ItemInCart> itemInCart = itemInCartService.findByCartStatusAndItemId(CartStatus.ACTIVE, itemId);
        return itemInCart
                .map(ItemInCart::getCount)
                .defaultIfEmpty(0);
    }

    @Transactional
    @Override
    public Mono<ItemDto> changeItemQuantityInCart(Long itemId, Action action) {
        return cartService.getOrCreateActiveCart()
                .flatMap(cart -> itemInCartService.findByCartIdAndItemId(cart.getCartId(), itemId)
                        .flatMap(itemInCart -> {
                            Mono<?> operation;
                            switch (action) {
                                case PLUS -> {
                                    itemInCart.setCount(itemInCart.getCount() + 1);
                                    operation = itemInCartService.save(itemInCart);
                                }
                                case MINUS -> {
                                    if (itemInCart.getCount() == 1) {
                                        operation = itemInCartService.delete(itemInCart.getItemInCartId());
                                    } else {
                                        itemInCart.setCount(itemInCart.getCount() - 1);
                                        operation = itemInCartService.save(itemInCart);
                                    }
                                }
                                case DELETE -> operation = itemInCartService.delete(itemInCart.getItemInCartId());
                                default ->
                                        operation = Mono.error(new IllegalStateException("Неизвестное действие: " + action));
                            }
                            return operation.then(recalculateCartTotal(cart));
                        })
                        .switchIfEmpty(
                                action == Action.PLUS
                                        ? itemService.getItemById(itemId)
                                        .flatMap(item ->
                                                itemInCartService.save(
                                                        ItemInCart.builder()
                                                                .cartId(cart.getCartId())
                                                                .itemId(item.getItemId())
                                                                .price(item.getPrice())
                                                                .count(1)
                                                                .build()
                                                )
                                        )
                                        .then(recalculateCartTotal(cart))
                                        : Mono.empty()
                        )
                )
                .then(getItemDto(itemId));
    }

    @Transactional
    @Override
    public Mono<OrderDto> createOrder() {
        return cartService.getOrCreateActiveCart()
                .flatMap(cart -> {
                    Order order = Order.builder()
                            .totalSum(cart.getTotal())
                            .build();
                    return orderService.save(order)
                            .flatMap(savedOrder ->
                                    itemInCartService.findAllByCartId(cart.getCartId())
                                            .map(itemInCart -> new ItemInOrder(
                                                    null,
                                                    itemInCart.getItemId(),
                                                    savedOrder.getOrderId(),
                                                    itemInCart.getPrice(),
                                                    itemInCart.getCount()
                                            ))
                                            .flatMap(itemInOrderService::save)
                                            .flatMap(savedItemInOrder ->
                                                    itemService.getItemById(savedItemInOrder.getItemId())
                                                            .map(item -> {
                                                                ItemDto itemDto = new ItemDto(
                                                                        item.getItemId(),
                                                                        item.getTitle(),
                                                                        item.getDescription(),
                                                                        item.getImgPath(),
                                                                        item.getPrice(),
                                                                        savedItemInOrder.getCount()
                                                                );
                                                                return new ItemInOrderDto(
                                                                        savedItemInOrder.getItemInOrderId(),
                                                                        itemDto,
                                                                        savedItemInOrder.getPrice(),
                                                                        savedItemInOrder.getCount()
                                                                );
                                                            })
                                            )
                                            .collect(Collectors.toSet())
                                            .map(itemsInOrder -> new OrderDto(
                                                    savedOrder.getOrderId(),
                                                    itemsInOrder,
                                                    savedOrder.getTotalSum()
                                            ))
                                            .flatMap(orderDto ->
                                                    cartService.closeCart()
                                                            .thenReturn(orderDto)
                                            )
                            );
                });
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<OrderDto> getOrderById(Long orderId) {
        return orderService.getOrderById(orderId)
                .flatMap(order -> {
                    return itemInOrderService.getItemInOrderByOrderId(order.getOrderId())
                            .flatMap(itemInOrder -> {
                                return itemService.getItemById(itemInOrder.getItemId())
                                        .map(item -> {
                                            ItemDto itemDto = new ItemDto(
                                                    item.getItemId(),
                                                    item.getTitle(),
                                                    item.getDescription(),
                                                    item.getImgPath(),
                                                    item.getPrice(),
                                                    itemInOrder.getCount()
                                            );
                                            return new ItemInOrderDto(
                                                    itemInOrder.getItemInOrderId(),
                                                    itemDto,
                                                    itemInOrder.getPrice(),
                                                    itemInOrder.getCount()
                                            );
                                        });
                            }).collect(Collectors.toSet())
                            .map(itemsInOrder -> new OrderDto(
                                    order.getOrderId(),
                                    itemsInOrder,
                                    order.getTotalSum()
                            ));
                })
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Заказ с id = " + orderId + " не найден")));
    }

    @Override
    public Mono<CartDto> getActiveCartDto() {
        return cartService.getOrCreateActiveCart()
                .flatMap(cart ->
                        itemInCartService.findAllByCartId(cart.getCartId())
                                .flatMap(itemInCart -> itemService.getItemById(itemInCart.getItemId())
                                        .map(item -> itemMapper.toItemDto(item, itemInCart.getCount())))
                                .collect(Collectors.toSet())
                                .map(items -> new CartDto(cart.getCartId(), items, cart.getTotal()))
                );
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<OrderDto> getOrders() {
        return orderService.getOrders()
                .flatMap(order -> {
                    return itemInOrderService.getItemInOrderByOrderId(order.getOrderId())
                            .flatMap(itemInOrder ->
                                    itemService.getItemById(itemInOrder.getItemId())
                                            .map(item -> new ItemInOrderDto(
                                                    itemInOrder.getItemInOrderId(),
                                                    itemMapper.toItemDto(item, itemInOrder.getCount()),
                                                    itemInOrder.getPrice(),
                                                    itemInOrder.getCount())))
                            .collect(Collectors.toSet())
                            .map(itemsInOrderDto -> new OrderDto(
                                    order.getOrderId(),
                                    itemsInOrderDto,
                                    order.getTotalSum()
                            ));
                });
    }

    private Mono<ItemDto> getItemDto(Long itemId) {
        Mono<Item> item = itemService.getItemById(itemId);
        Mono<Integer> itemInCartCount = getItemInCartCount(itemId);
        return Mono.zip(item, itemInCartCount)
                .map(tuple -> itemMapper.toItemDto(tuple.getT1(), tuple.getT2()));
    }

    private Mono<Cart> recalculateCartTotal(Cart cart) {
        return itemInCartService.findAllByCartId(cart.getCartId())
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .flatMap(total -> {
                    cart.setTotal(total);
                    return cartService.save(cart);
                });
    }

    private List<List<ItemDto>> splitByThree(List<ItemDto> items) {
        List<List<ItemDto>> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i += 3) {
            List<ItemDto> subResult = new ArrayList<>(items.subList(i, Math.min(i + 3, items.size())));
            while (subResult.size() < 3) {
                subResult.add(new ItemDto(-1L, null, null, null, null, null));
            }
            result.add(subResult);
        }
        return result;
    }

}