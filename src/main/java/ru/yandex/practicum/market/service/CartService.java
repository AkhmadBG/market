package ru.yandex.practicum.market.service;

import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.enums.Action;

public interface CartService {

    CartDto getItemsInCart();

    CartDto changeItemsQuantityInCart(Long itemId, Action action);

//    CartDto getCartDto();

    void clearCart();

    Cart getActiveCart();

}