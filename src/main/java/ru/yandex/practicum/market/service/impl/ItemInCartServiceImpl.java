package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    public Mono<ItemInCart> findByCartStatusAndItemId(CartStatus cartStatus, Long itemId) {
        return itemInCartRepository.findByCart_CartStatusAndItem_ItemId(cartStatus, itemId);
    }

    @Override
    public Flux<ItemInCart> findAllByCartId(Long cartId) {
        return null;
    }

    @Override
    public Mono<ItemInCart> save(ItemInCart itemInCart) {
        return itemInCartRepository.save(itemInCart);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return null;
    }

}