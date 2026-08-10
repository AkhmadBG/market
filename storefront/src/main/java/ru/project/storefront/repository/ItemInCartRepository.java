package ru.project.storefront.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.ItemInCart;

@Repository
public interface ItemInCartRepository extends ReactiveCrudRepository<ItemInCart, Long> {

    Flux<ItemInCart> findAllByCartId(Long cartId);

    Mono<ItemInCart> findByCartIdAndItemId(Long cartId, Long itemId);

}