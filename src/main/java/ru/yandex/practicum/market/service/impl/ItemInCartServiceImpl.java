package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.service.ItemInCartService;

@Service
@RequiredArgsConstructor
public class ItemInCartServiceImpl implements ItemInCartService {

    private final ItemInCartRepository itemInCartRepository;

    @Override
    public Mono<ItemInCart> findByCartStatusAndItemId(CartStatus cartStatus, Long itemId) {
        return itemInCartRepository.findByCartStatusAndItemId(cartStatus, itemId);
    }

    @Override
    public Flux<ItemInCart> findAllByCartId(Long cartId) {
        return itemInCartRepository.findAllByCartId(cartId);
    }

    @Override
    public Mono<ItemInCart> save(ItemInCart itemInCart) {
        return itemInCartRepository.save(itemInCart);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return itemInCartRepository.deleteById(id);
    }

    @Override
    public Mono<ItemInCart> findByCartIdAndItemId(Long cartId, Long itemId) {
        return itemInCartRepository.findByCartIdAndItemId(cartId, itemId);
    }

}