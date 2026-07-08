package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.exception.CartNotFoundException;
import ru.yandex.practicum.market.mapper.CartMapper;
import ru.yandex.practicum.market.repository.CartRepository;
import ru.yandex.practicum.market.service.CartService;
import ru.yandex.practicum.market.service.ItemInCartService;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ItemInCartService itemInCartService;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Transactional
    @Override
    public CartDto getItemsInCart() {
        return getCartDto();
    }

    @Transactional
    @Override
    public CartDto changeItemsQuantityInCart(Long itemId, Action action) {
        ItemInCart itemInCart = itemInCartService.getItemInCartByItemId(itemId);
        switch (action) {
            case PLUS -> itemInCart.setCount(itemInCart.getCount() + 1);
            case MINUS -> {
                if (itemInCart.getCount() == 1 || itemInCart.getCount() == 0) {
                    itemInCart.setCount(0);
                } else {
                    itemInCart.setCount(itemInCart.getCount() - 1);
                }
            }
            case DELETE -> itemInCart.setCount(0);
            default -> throw new IllegalStateException("Значения " + action + " нет в enum Action");
        }
        return getCartDto();
    }

    @Transactional
    @Override
    public CartDto getCartDto() {
        Set<ItemInCart> itemsInCart = itemInCartService.getItemsICartByCart();
        BigDecimal total = itemsInCart.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return cartMapper.toCartDto(itemsInCart, total);
    }

    @Transactional
    @Override
    public void clearCart() {
        Cart cart = getActiveCart();
        cart.setCartStatus(CartStatus.CLOSED);
    }

    @Override
    public Cart getActiveCart() {
        return cartRepository.findByCartStatus(CartStatus.ACTIVE)
                .orElseThrow(() -> new CartNotFoundException("Активная корзина не найдена"));
    }

}