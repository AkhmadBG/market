package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    @Override
    public CartDto getItemsInCart() {
        Cart cart = getActiveCart();
        return cartMapper.toCartDto(cart);
    }

    @Transactional
    @Override
    public void closeCart() {
        Cart cart = getActiveCart();
        cart.setCartStatus(CartStatus.CLOSED);
    }

    @Transactional
    @Override
    public Cart getActiveCart() {
        return cartRepository.findByCartStatus(CartStatus.ACTIVE)
                .orElseGet(
                        () -> cartRepository.save(
                                Cart.builder()
                                        .total(BigDecimal.ZERO)
                                        .cartStatus(CartStatus.ACTIVE)
                                        .itemsInCart(new HashSet<>())
                                        .build()
                        )
                );
    }

}