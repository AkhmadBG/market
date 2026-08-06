package ru.project.storefront.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.Order;

public interface OrderService {

    Flux<Order> getOrders();

    Mono<Order> save(Order order);

    Mono<Order> getOrderById(Long orderId);

}