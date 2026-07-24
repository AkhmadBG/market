package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.Order;
import ru.yandex.practicum.market.mapper.OrderMapper;
import ru.yandex.practicum.market.repository.OrderRepository;
import ru.yandex.practicum.market.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    @Override
    public Flux<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Mono<Order> save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Mono<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

}