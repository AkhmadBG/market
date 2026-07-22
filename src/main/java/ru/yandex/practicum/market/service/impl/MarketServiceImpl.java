package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.*;
import ru.yandex.practicum.market.entity.*;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.mapper.OrderMapper;
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
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public Mono<ItemsPageDto> getItems(String search, Pageable pageable) {
        Mono<SearchResult> items = itemService.search(search, pageable);
        Mono<CartDto> cart = cartService.getActiveCartDto();
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
                                    pageable.getPageNumber() + 1,
                                    pageable.getPageSize(),
                                    pageable.hasPrevious(),
                                    pageable.getOffset() + pageable.getPageSize() < result.total()
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
    public Mono<ItemDto> changeItemQuantityInItem(Long itemId, Action action) {
        changeItemQuantityInCart(itemId, action);
        return getItemDto(itemId);
    }

    private Mono<ItemDto> getItemDto(Long itemId) {
        Mono<Item> item = itemService.getItemById(itemId);
        Mono<Integer> itemInCartCount = getItemInCartCount(itemId);
        return Mono.zip(item, itemInCartCount)
                .map(tuple -> itemMapper.toItemDto(tuple.getT1(), tuple.getT2()));
    }

    @Transactional
    @Override
    public Mono<Void> changeItemQuantityInCart(Long itemId, Action action) {
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
                .then();
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
                            .flatMap(saveOrder ->
                                    itemInCartService.findAllByCartId(cart.getCartId())
                                            .map(itemMapper::toItemInOrder)
                                            .doOnNext(itemInOrder -> itemInOrder.setOrderId(saveOrder.getOrderId()))
                                            .flatMap(itemInOrderService::save)
                                            .then(cartService.closeCart())
                                            .thenReturn(orderMapper.toOrderDto(saveOrder))

                            );
                });
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