package ru.project.storefront.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.repository.CartRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void shouldCreateCartWhenActiveCartNotExists() {
        Cart newCart = Cart.builder()
                .cartId(1L)
                .total(BigDecimal.ZERO)
                .cartStatus(CartStatus.ACTIVE)
                .build();

        when(cartRepository.findByCartStatus(CartStatus.ACTIVE))
                .thenReturn(Mono.empty());

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(Mono.just(newCart));

        StepVerifier.create(cartService.getOrCreateActiveCart())
                .assertNext(result -> {
                    assertEquals(BigDecimal.ZERO, result.getTotal());
                    assertEquals(CartStatus.ACTIVE, result.getCartStatus());
                })
                .verifyComplete();

        verify(cartRepository).findByCartStatus(CartStatus.ACTIVE);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void shouldCloseCart() {
        Cart activeCart = Cart.builder()
                .cartId(1L)
                .total(BigDecimal.TEN)
                .cartStatus(CartStatus.ACTIVE)
                .build();

        Cart closedCart = Cart.builder()
                .cartId(1L)
                .total(BigDecimal.TEN)
                .cartStatus(CartStatus.CLOSED)
                .build();

        when(cartRepository.findByCartStatus(CartStatus.ACTIVE))
                .thenReturn(Mono.just(activeCart));

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(Mono.just(closedCart));

        StepVerifier.create(cartService.closeCart())
                .verifyComplete();

        assertEquals(CartStatus.CLOSED, activeCart.getCartStatus());

        verify(cartRepository).findByCartStatus(CartStatus.ACTIVE);
        verify(cartRepository).save(activeCart);
    }

    @Test
    void shouldSaveCart() {
        Cart cart = Cart.builder()
                .cartId(1L)
                .total(BigDecimal.ONE)
                .cartStatus(CartStatus.ACTIVE)
                .build();

        when(cartRepository.save(cart))
                .thenReturn(Mono.just(cart));

        StepVerifier.create(cartService.save(cart))
                .expectNext(cart)
                .verifyComplete();

        verify(cartRepository).save(cart);
    }
}