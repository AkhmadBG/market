package ru.project.storefront.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.ItemInOrder;

public interface ItemInOrderService {

    Mono<ItemInOrder> save(ItemInOrder itemInOrder);

    Flux<ItemInOrder> getItemInOrderByOrderId(Long orderId);

}