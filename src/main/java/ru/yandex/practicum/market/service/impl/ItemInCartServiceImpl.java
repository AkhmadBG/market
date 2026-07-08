package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.exception.ItemInCartNotFoundException;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.service.ItemInCartService;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemInCartServiceImpl implements ItemInCartService {

    private final ItemInCartRepository itemInCartRepository;

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
    public ItemInCart getItemInCartByItemId(Long itemId) {
        return itemInCartRepository.findByCart_CartStatusAndItem_ItemId(CartStatus.ACTIVE, itemId)
                .orElseThrow(() -> new ItemInCartNotFoundException("Товар с id = " + itemId + " не найден в корзине"));
    }

    @Override
    public Set<ItemInCart> getItemsICartByCart() {
        return itemInCartRepository.findAllByCart_CartStatus(CartStatus.ACTIVE);
    }

}