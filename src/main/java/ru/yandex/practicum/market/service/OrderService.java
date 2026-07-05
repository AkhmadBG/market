package ru.yandex.practicum.market.service;

import ru.yandex.practicum.market.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> getOrders();

    OrderDto getOrderById(Long orderId, boolean newOrder);

    Long newOrder();

}