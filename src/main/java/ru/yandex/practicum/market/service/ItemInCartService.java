package ru.yandex.practicum.market.service;

import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.CartStatus;

import java.util.Optional;

public interface ItemInCartService {

    Optional<ItemInCart> findByCart_CartStatusAndItem_ItemId(CartStatus cartStatus, Long itemId);

    void save(ItemInCart itemInCart);

}