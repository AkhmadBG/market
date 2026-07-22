package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Order;
import ru.yandex.practicum.market.exception.OrderNotFoundException;
import ru.yandex.practicum.market.mapper.OrderMapper;
import ru.yandex.practicum.market.repository.OrderRepository;
import ru.yandex.practicum.market.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    @Override
    public Flux<OrderDto> getOrders() {
        return orderRepository.findAll()
                .map(orderMapper::toOrderDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<OrderDto> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderDto)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Заказ с id = " + orderId + " не найден")));
    }

    @Override
    public Mono<Order> save(Order order) {
        return orderRepository.save(order);
    }

}