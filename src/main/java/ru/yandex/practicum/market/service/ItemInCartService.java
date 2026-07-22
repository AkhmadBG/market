package ru.yandex.practicum.market.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.CartStatus;

public interface ItemInCartService {

    Mono<ItemInCart> findByCartStatusAndItemId(CartStatus cartStatus, Long itemId);

    Flux<ItemInCart> findAllByCartId(Long cartId);

    Mono<ItemInCart> save(ItemInCart item);

    Mono<Void> delete(Long id);

    Mono<ItemInCart> findByCartIdAndItemId(Long cartId, Long itemId);

}