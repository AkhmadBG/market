package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.service.ItemInCartService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemInCartServiceImpl implements ItemInCartService {

    private final ItemInCartRepository itemInCartRepository;

    @Override
    public Optional<ItemInCart> findByCart_CartStatusAndItem_ItemId(CartStatus cartStatus, Long itemId) {
        return itemInCartRepository.findByCart_CartStatusAndItem_ItemId(cartStatus, itemId);
    }

    @Override
    public void save(ItemInCart itemInCart) {
        itemInCartRepository.save(itemInCart);
    }

}