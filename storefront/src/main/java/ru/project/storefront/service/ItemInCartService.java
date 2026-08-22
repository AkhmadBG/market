package ru.project.storefront.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.ItemInCart;
import ru.project.storefront.enums.CartStatus;

public interface ItemInCartService {

    Mono<ItemInCart> findByUserIdAndCartStatusAndItemId(Long userId, CartStatus cartStatus, Long itemId);

    Flux<ItemInCart> findAllByCartId(Long cartId);

    Mono<ItemInCart> save(ItemInCart item);

    Mono<Void> delete(Long id);

    Mono<ItemInCart> findByCartIdAndItemId(Long cartId, Long itemId);

}