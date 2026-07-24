package ru.yandex.practicum.market.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.ItemSort;

public interface MarketService {

    Mono<ItemsPageDto> getItems(String search, ItemSort itemSort, int pageNumber, int pageSize);

    Mono<ItemDto> getItemDtoById(Long itemId);

    Mono<Integer> getItemInCartCount(Long itemId);

    Mono<Mono<ItemDto>> changeItemQuantityInCart(Long itemId, Action action);

    Mono<OrderDto> createOrder();

    Mono<OrderDto> getOrderById(Long orderId);

    Mono<CartDto> getActiveCartDto();

    Flux<OrderDto> getOrders();

}