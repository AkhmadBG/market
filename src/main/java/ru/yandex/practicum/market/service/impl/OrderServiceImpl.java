package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.entity.ItemInOrder;
import ru.yandex.practicum.market.entity.Order;
import ru.yandex.practicum.market.exception.OrderNotFoundException;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.mapper.OrderMapper;
import ru.yandex.practicum.market.repository.ItemInOrderRepository;
import ru.yandex.practicum.market.repository.OrderRepository;
import ru.yandex.practicum.market.service.CartService;
import ru.yandex.practicum.market.service.OrderService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
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
        Cart cart = cartService.getCart();
        Set<ItemInOrder> itemsInOrder = cart.getItemsInCart()
                .stream()
                .map(itemMapper::toItemInOrder)
                .collect(Collectors.toSet());
        Order order = Order.builder()
                .itemsInOrder(itemsInOrder)
                .totalSum(cart.getTotal())
                .build();
        Order newOrder = orderRepository.save(order);
        for(ItemInOrder itemInOrder : itemsInOrder) {
            itemInOrder.setOrder(newOrder);
            itemInOrderRepository.save(itemInOrder);
        }
        cartService.deleteCartById(cart.getCartId());
        return newOrder.getOrderId();
    }

}