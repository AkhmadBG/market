package ru.project.storefront.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.Order;
import ru.project.storefront.repository.OrderRepository;
import ru.project.storefront.service.OrderService;

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