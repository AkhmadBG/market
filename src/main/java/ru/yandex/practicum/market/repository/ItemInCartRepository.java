package ru.yandex.practicum.market.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.CartStatus;

@Repository
public interface ItemInCartRepository extends ReactiveCrudRepository<ItemInCart, Long> {

    Mono<ItemInCart> findByCartStatusAndItemId(CartStatus cartStatus, Long itemId);

    Flux<ItemInCart> findAllByCartId(Long cartId);

    Mono<ItemInCart> findByCartIdAndItemId(Long cartId, Long itemId);

}