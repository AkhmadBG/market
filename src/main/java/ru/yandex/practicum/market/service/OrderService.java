package ru.yandex.practicum.market.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Order;

public interface OrderService {

    Flux<OrderDto> getOrders();

    Mono<OrderDto> getOrderById(Long orderId);

    Mono<Order> save(Order order);

}