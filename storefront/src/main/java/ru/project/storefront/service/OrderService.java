package ru.project.storefront.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.Order;

public interface OrderService {

    Flux<Order> getOrdersByUserId(Long userId);

    Mono<Order> save(Order order);

    Mono<Order> getOrderByIdAndUserId(Long orderId, Long userId);

}