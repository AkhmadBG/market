package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.mapper.CartMapper;
import ru.yandex.practicum.market.repository.CartRepository;
import ru.yandex.practicum.market.service.CartService;

import java.math.BigDecimal;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Transactional
    public Mono<CartDto> getActiveCartDto() {
        return getOrCreateActiveCart()
                .map(cartMapper::toCartDto);
    }

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

}