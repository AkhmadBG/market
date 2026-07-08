package ru.yandex.practicum.market.service;

import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;

import java.util.Set;

public interface ItemInCartService {

    Integer getItemInCartCount(Long itemId);

    ItemInCart getItemInCartByItemId(Long itemId);

    Set<ItemInCart> getItemsICartByCart();

}