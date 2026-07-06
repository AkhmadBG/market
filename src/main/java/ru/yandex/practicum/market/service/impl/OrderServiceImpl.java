package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInOrder;
import ru.yandex.practicum.market.entity.Order;
import ru.yandex.practicum.market.exception.OrderNotFoundException;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.mapper.OrderMapper;
import ru.yandex.practicum.market.repository.ItemInOrderRepository;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.repository.OrderRepository;
import ru.yandex.practicum.market.service.CartService;
import ru.yandex.practicum.market.service.OrderService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ItemInOrderRepository itemInOrderRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;

    @Override
    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long orderId, boolean newOrder) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заказ с id = " + orderId + " не найден"));
        return orderMapper.toOrderDto(order);
    }

    @Transactional
    @Override
    public Long newOrder() {
        Set<Item> items = itemRepository.findByCountGreaterThan(0);
        Set<ItemInOrder> itemsInOrder = new HashSet<>();
        for (Item item : items) {
            ItemInOrder itemInOrder = itemMapper.toItemInOrder(item);
            ItemInOrder saveItemInOrder = itemInOrderRepository.save(itemInOrder);
            itemsInOrder.add(saveItemInOrder);
        }
        BigDecimal total = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = Order.builder()
                .itemsInOrder(itemsInOrder)
                .totalSum(total)
                .build();
        Order newOrder = orderRepository.save(order);
        for (ItemInOrder itemInOrder : itemsInOrder) {
            itemInOrder.setOrder(newOrder);
        }
        cartService.deleteCart();
        return newOrder.getOrderId();
    }

}