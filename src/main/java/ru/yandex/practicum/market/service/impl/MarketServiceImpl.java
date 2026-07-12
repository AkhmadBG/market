package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.dto.PagingDto;
import ru.yandex.practicum.market.entity.*;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.exception.CartIsEmptyException;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.mapper.CartMapper;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.mapper.OrderMapper;
import ru.yandex.practicum.market.service.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public ItemDto getItemDtoById(Long itemId) {
        Item item = itemService.getItemById(itemId);
        Integer itemInCartCount = getItemInCartCount(itemId);
        BigDecimal total = item.getPrice().multiply(BigDecimal.valueOf(itemInCartCount));
        return itemMapper.toItemDto(item, itemInCartCount, total);
    }

    @Transactional
    @Override
    public ItemDto changeItemQuantityInItem(Long itemId, Action action) {
        changeItemsQuantityInCart(itemId, action);
        Integer itemInCartCount = getItemInCartCount(itemId);
        Item item = itemService.getItemById(itemId);
        BigDecimal total = item.getPrice().multiply(BigDecimal.valueOf(itemInCartCount));
        return itemMapper.toItemDto(item, itemInCartCount, total);
    }

    @Transactional
    @Override
    public ItemsPageDto getItems(String search, Pageable pageable) {
        Page<Item> itemsPage = itemService.findByTitleOrDescription(search, search, pageable);
        Cart cart = cartService.getActiveCart();
        Map<Long, Integer> counts = cart.getItemsInCart().stream()
                .collect(Collectors.toMap(
                        item -> item.getItem().getItemId(),
                        ItemInCart::getCount
                ));

        List<ItemDto> itemsDto = itemsPage.getContent().stream()
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
                        itemsPage.getNumber() + 1,
                        itemsPage.getSize(),
                        itemsPage.hasPrevious(),
                        itemsPage.hasNext()
                ));
    }

    @Transactional
    @Override
    public Long newOrder() {
        Cart cart = cartService.getActiveCart();

        if (cart.getItemsInCart().isEmpty()) {
            throw new CartIsEmptyException("Корзина пуста");
        }

        Order order = new Order();

        for (ItemInCart itemInCart : cart.getItemsInCart()) {
            ItemInOrder itemInOrder = itemMapper.toItemInOrder(itemInCart);
            ItemInOrder saveItemInOrder = itemInOrderService.save(itemInOrder);
            order.addItemInOrder(saveItemInOrder);
        }

        order.setTotalSum(cart.getTotal());

        Order newOrder = orderService.save(order);

        cartService.clearCart();
        return newOrder.getOrderId();
    }

    @Transactional
    @Override
    public void changeItemsQuantityInCart(Long itemId, Action action) {
        Cart cart = cartService.getActiveCart();

        Item item = itemService.getItemById(itemId);

        ItemInCart itemInCart = cart.getItemsInCart().stream()
                .filter(i -> i.getItem().getItemId().equals(itemId))
                .findFirst()
                .orElseGet(() -> ItemInCart.builder()
                        .cart(cart)
                        .item(item)
                        .count(0)
                        .price(BigDecimal.ZERO)
                        .build());

        itemInCartService.save(itemInCart);

        switch (action) {
            case PLUS -> {
                itemInCart.setCount(itemInCart.getCount() + 1);
                itemInCartService.save(itemInCart);
                itemInCart.setPrice(itemInCart.getItem().getPrice().multiply(BigDecimal.valueOf(itemInCart.getCount())));
            }
            case MINUS -> {
                if (itemInCart.getCount() == 1 || itemInCart.getCount() == 0) {
                    cart.getItemsInCart().remove(itemInCart);
                } else {
                    itemInCart.setCount(itemInCart.getCount() - 1);
                    itemInCartService.save(itemInCart);
                    itemInCart.setPrice(itemInCart.getItem().getPrice().multiply(BigDecimal.valueOf(itemInCart.getCount())));
                }
            }
            case DELETE -> cart.getItemsInCart().remove(itemInCart);
            default -> throw new IllegalStateException("Значения " + action + " нет в enum Action");
        }

        BigDecimal total = cart.getItemsInCart().stream()
                .map(
                        i -> i.getItem().getPrice()
                                .multiply(BigDecimal.valueOf(i.getCount()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotal(total);
    }

    @Override
    public Integer getItemInCartCount(Long itemId) {
        Optional<ItemInCart> itemInCartOpt = itemInCartService.findByCart_CartStatusAndItem_ItemId(CartStatus.ACTIVE, itemId);
        if (itemInCartOpt.isPresent()) {
            return itemInCartOpt.get().getCount();
        } else {
            return 0;
        }
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