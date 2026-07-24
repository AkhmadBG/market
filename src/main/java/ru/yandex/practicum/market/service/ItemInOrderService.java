package ru.yandex.practicum.market.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.ItemInOrder;

public interface ItemInOrderService {

    Mono<ItemInOrder> save(ItemInOrder itemInOrder);

    Flux<ItemInOrder> getItemInOrderByOrderId(Long orderId);

}