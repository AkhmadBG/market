package ru.yandex.practicum.market.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.Cart;

public interface CartService {


    Mono<Void> closeCart();

    Mono<Cart> getOrCreateActiveCart();

    Mono<Cart> save(Cart cart);

}