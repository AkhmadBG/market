package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.entity.ItemInOrder;
import ru.yandex.practicum.market.entity.Order;
import ru.yandex.practicum.market.exception.CartIsEmptyException;
import ru.yandex.practicum.market.exception.OrderNotFoundException;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.mapper.OrderMapper;
import ru.yandex.practicum.market.repository.ItemInOrderRepository;
import ru.yandex.practicum.market.repository.OrderRepository;
import ru.yandex.practicum.market.service.CartService;
import ru.yandex.practicum.market.service.ItemInOrderService;
import ru.yandex.practicum.market.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
//    private final ItemInOrderService itemInOrderService;
//    private final CartService cartService;
    private final OrderMapper orderMapper;
//    private final ItemMapper itemMapper;

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заказ с id = " + orderId + " не найден"));
        return orderMapper.toOrderDto(order);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

//    @Transactional
//    @Override
//    public Long newOrder() {
//        Cart cart = cartService.getActiveCart();
//
//        if (cart.getItemsInCart().isEmpty()) {
//            throw new CartIsEmptyException("Корзина пуста");
//        }
//
//        Order order = new Order();
//
//        for (ItemInCart itemInCart : cart.getItemsInCart()) {
//            ItemInOrder itemInOrder = itemMapper.toItemInOrder(itemInCart);
//            ItemInOrder saveItemInOrder = itemInOrderService.save(itemInOrder);
//            order.addItemInOrder(saveItemInOrder);
//        }
//
//        order.setTotalSum(cart.getTotal());
//
//        Order newOrder = orderRepository.save(order);
//
//        cartService.clearCart();
//        return newOrder.getOrderId();
//    }

}