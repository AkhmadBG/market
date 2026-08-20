package ru.project.storefront.service;

import reactor.core.publisher.Mono;
import ru.project.storefront.entity.Cart;

public interface CartService {

    Mono<Void> closeCart(Long userId);

    Mono<Cart> getOrCreateActiveCart(Long userId);

    Mono<Cart> save(Cart cart);

}