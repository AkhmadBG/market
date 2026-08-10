package ru.project.storefront.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.Cart;
import ru.project.storefront.enums.CartStatus;
import ru.project.storefront.repository.CartRepository;
import ru.project.storefront.service.CartService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Transactional
    @Override
    public Mono<Void> closeCart() {
        return getOrCreateActiveCart()
                .flatMap(cart -> {
                    cart.setCartStatus(CartStatus.CLOSED);
                    return cartRepository.save(cart);
                }).then();
    }

    @Transactional
    @Override
    public Mono<Cart> getOrCreateActiveCart() {
        return cartRepository.findByCartStatus(CartStatus.ACTIVE)
                .switchIfEmpty(
                        cartRepository.save(
                                Cart.builder()
                                        .total(BigDecimal.ZERO)
                                        .cartStatus(CartStatus.ACTIVE)
                                        .build()
                        )
                );
    }

    @Override
    public Mono<Cart> save(Cart cart) {
        return cartRepository.save(cart);
    }

}