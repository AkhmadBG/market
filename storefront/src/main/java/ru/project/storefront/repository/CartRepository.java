package ru.project.storefront.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.Cart;
import ru.project.storefront.enums.CartStatus;

@Repository
public interface CartRepository extends ReactiveCrudRepository<Cart, Long> {

    Mono<Cart> findByCartStatus(CartStatus cartStatus);

}