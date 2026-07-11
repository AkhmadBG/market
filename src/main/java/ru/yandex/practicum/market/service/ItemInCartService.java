package ru.yandex.practicum.market.service;

import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.Action;

import java.util.Set;

public interface ItemInCartService {

    Integer getItemInCartCount(Long itemId);

    Set<ItemInCart> getItemsInCartByCart();

    Item changeItemsQuantityInCart(Long itemId, Action action);

}