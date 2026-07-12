package ru.yandex.practicum.market.service;

import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Order;

import java.util.List;

public interface OrderService {

    List<OrderDto> getOrders();

    OrderDto getOrderById(Long orderId);

    Order save(Order order);

}