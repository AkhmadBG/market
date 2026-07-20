package ru.yandex.practicum.market.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.enums.Action;

public interface MarketService {

    Mono<ItemsPageDto> getItems(String search, Pageable pageable);

    Mono<ItemDto> getItemDtoById(Long itemId);

    Mono<Integer> getItemInCartCount(Long itemId);

    Mono<ItemDto> changeItemQuantityInItem(Long itemId, Action action);

    Mono<Void> changeItemQuantityInCart(Long itemId, Action action);

    Mono<OrderDto> createOrder();

}