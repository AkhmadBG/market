package ru.project.storefront.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.dto.CartDto;
import ru.project.storefront.dto.ItemDto;
import ru.project.storefront.dto.ItemsPageDto;
import ru.project.storefront.dto.OrderDto;
import ru.project.storefront.enums.Action;
import ru.project.storefront.enums.ItemSort;

public interface MarketService {

    Mono<ItemsPageDto> getItems(String search, ItemSort itemSort, int pageNumber, int pageSize);

    Mono<ItemsPageDto> getItemsForAuthenticationUser(String search, ItemSort itemSort, int pageNumber, int pageSize);

    Mono<ItemDto> getItemDtoById(Long itemId);

    Mono<Integer> getItemInCartCount(Long itemId);

    Mono<ItemDto> changeItemQuantityInCart(Long itemId, Action action);

    Mono<OrderDto> createOrder();

    Mono<OrderDto> getOrderById(Long orderId);

    Mono<CartDto> getActiveCartDto();

    Flux<OrderDto> getOrders();

}