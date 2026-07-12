package ru.yandex.practicum.market.service;

import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;

public interface CartService {

    CartDto getItemsInCart();

    void closeCart();

    Cart getActiveCart();

}