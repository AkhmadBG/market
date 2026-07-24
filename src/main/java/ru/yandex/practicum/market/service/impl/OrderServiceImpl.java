package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.Order;
import ru.yandex.practicum.market.repository.OrderRepository;
import ru.yandex.practicum.market.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public Flux<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> save(Order order) {
        return orderRepository.save(order);
    }

    public Mono<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

}