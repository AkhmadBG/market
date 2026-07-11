package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.exception.CartNotFoundException;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.repository.CartRepository;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.ItemInCartService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemInCartServiceImpl implements ItemInCartService {

    private final ItemInCartRepository itemInCartRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final ItemMapper itemMapper;

    @Override
    public Integer getItemInCartCount(Long itemId) {
        Optional<ItemInCart> itemInCartOpt = itemInCartRepository.findByCart_CartStatusAndItem_ItemId(CartStatus.ACTIVE, itemId);
        if (itemInCartOpt.isPresent()) {
            return itemInCartOpt.get().getCount();
        } else {
            return 0;
        }
    }

    @Override
    public Set<ItemInCart> getItemsInCartByCart() {
        return itemInCartRepository.findAllByCart_CartStatus(CartStatus.ACTIVE);
    }

    @Override
    public Item changeItemsQuantityInCart(Long itemId, Action action) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден"));
        ItemInCart itemInCart = getItemInCartByItemId(itemId);
        switch (action) {
            case PLUS -> {
                itemInCart.setCount(itemInCart.getCount() + 1);
                itemInCart.setPrice(item.getPrice().multiply(BigDecimal.valueOf(itemInCart.getCount())));
            }
            case MINUS -> {
                if (itemInCart.getCount() == 1 || itemInCart.getCount() == 0) {
                    deleteItemFromCart(itemInCart);
                } else {
                    itemInCart.setCount(itemInCart.getCount() - 1);
                    itemInCart.setPrice(item.getPrice().multiply(BigDecimal.valueOf(itemInCart.getCount())));
                }
            }
            case DELETE -> deleteItemFromCart(itemInCart);
            default -> throw new IllegalStateException("Значения " + action + " нет в enum Action");
        }
        return item;
    }

    private void deleteItemFromCart(ItemInCart itemInCart) {
        itemInCartRepository.delete(itemInCart);
    }

    private ItemInCart getItemInCartByItemId(Long itemId) {
        Optional<ItemInCart> itemInCartOpt = itemInCartRepository.findByCart_CartStatusAndItem_ItemId(CartStatus.ACTIVE, itemId);
        if (itemInCartOpt.isPresent()) {
            return itemInCartOpt.get();
        } else {
            Cart cart = cartRepository.findByCartStatus(CartStatus.ACTIVE)
                    .orElseThrow(() -> new CartNotFoundException("Активная корзина не найдена"));
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден"));
            ItemInCart itemInCart = itemMapper.toItemInCart(item, cart);
            return itemInCartRepository.save(itemInCart);
        }
    }

}