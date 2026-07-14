package ru.yandex.practicum.market.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.mapper.CartMapper;
import ru.yandex.practicum.market.repository.CartRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = Cart.builder()
                .cartId(1L)
                .cartStatus(CartStatus.ACTIVE)
                .total(BigDecimal.TEN)
                .itemsInCart(new HashSet<>())
                .build();
    }

    @Test
    void getItemsInCart_shouldReturnCartDto() {
        CartDto cartDto = mock(CartDto.class);

        when(cartRepository.findByCartStatus(CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));
        when(cartMapper.toCartDto(cart))
                .thenReturn(cartDto);

        CartDto result = cartService.getItemsInCart();

        assertEquals(cartDto, result);

        verify(cartRepository).findByCartStatus(CartStatus.ACTIVE);
        verify(cartMapper).toCartDto(cart);
    }

    @Test
    void getActiveCart_shouldReturnExistingCart() {
        when(cartRepository.findByCartStatus(CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        Cart result = cartService.getActiveCart();

        assertEquals(cart, result);

        verify(cartRepository).findByCartStatus(CartStatus.ACTIVE);
        verify(cartRepository, never()).save(any());
    }

    @Test
    void getActiveCart_shouldCreateCartIfNotExists() {
        Cart newCart = Cart.builder()
                .cartId(2L)
                .cartStatus(CartStatus.ACTIVE)
                .total(BigDecimal.ZERO)
                .itemsInCart(new HashSet<>())
                .build();

        when(cartRepository.findByCartStatus(CartStatus.ACTIVE))
                .thenReturn(Optional.empty());

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(newCart);

        Cart result = cartService.getActiveCart();

        assertEquals(newCart, result);

        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void closeCart_shouldChangeStatusToClosed() {
        when(cartRepository.findByCartStatus(CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        cartService.closeCart();

        assertEquals(CartStatus.CLOSED, cart.getCartStatus());

        verify(cartRepository).findByCartStatus(CartStatus.ACTIVE);
    }

}