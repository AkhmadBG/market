package ru.yandex.practicum.market.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;

public interface CartService {

    Mono<CartDto> getActiveCartDto();

    Mono<Void> closeCart();

    Mono<Cart> getOrCreateActiveCart();

}