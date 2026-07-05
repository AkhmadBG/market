package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.mapper.CartMapper;
import ru.yandex.practicum.market.repository.CartRepository;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.service.CartService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ItemInCartRepository itemInCartRepository;
    private final CartMapper cartMapper;

    @Override
    public CartDto getItemsInCart() {
        return cartMapper.toCartDto(getCart());
    }

    @Transactional
    @Override
    public CartDto changeItemsQuantityInCart(Long itemId, Action action) {
        ItemInCart itemInCart = itemInCartRepository.findByItem_ItemId(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден в корзине"));
        switch (action) {
            case PLUS -> {
                itemInCart.setCount(itemInCart.getCount() + 1);
                itemInCartRepository.save(itemInCart);
            }
            case MINUS -> {
                if (itemInCart.getCount() == 1) {
                    itemInCartRepository.deleteById(itemId);
                } else {
                    itemInCart.setCount(itemInCart.getCount() - 1);
                    itemInCartRepository.save(itemInCart);
                }
            }
            case DELETE -> itemInCartRepository.deleteById(itemId);
            default -> throw new IllegalStateException("Значения " + action + " нет в enum Action");
        }
        return cartMapper.toCartDto(getCart());
    }

    @Transactional
    @Override
    public Cart getCart() {
        List<ItemInCart> allItemsInCart = itemInCartRepository.findAll();
        Set<ItemInCart> itemsInCart = new HashSet<>(allItemsInCart);
        BigDecimal totalProductsSum = itemsInCart.stream()
                .map(itemInCart -> itemInCart.getItem().getPrice()
                        .multiply(BigDecimal.valueOf(itemInCart.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Cart cart = Cart.builder()
                .itemsInCart(itemsInCart)
                .total(totalProductsSum)
                .build();
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void deleteCartById(Long cartId) {
        cartRepository.deleteById(cartId);
        itemInCartRepository.deleteAll();
    }

}