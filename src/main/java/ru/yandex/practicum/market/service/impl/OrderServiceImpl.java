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
import ru.yandex.practicum.market.service.ItemInCartService;
import ru.yandex.practicum.market.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ItemInOrderRepository itemInOrderRepository;
    private final OrderRepository orderRepository;
    private final ItemInCartService itemInCartService;
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
        Cart cart = cartService.getActiveCart();

        if (cart.getItemsInCart().isEmpty()) {
            throw new CartIsEmptyException("Корзина пуста");
        }

        Order order = new Order();

        for (ItemInCart itemInCart : cart.getItemsInCart()) {
            ItemInOrder itemInOrder = itemMapper.toItemInOrder(itemInCart);
            ItemInOrder saveItemInOrder = itemInOrderRepository.save(itemInOrder);
            order.addItemInOrder(saveItemInOrder);
        }

        BigDecimal totalSum = cart.getItemsInCart().stream()
                .map(
                        itemInCart -> itemInCart.getPrice().multiply(
                                BigDecimal.valueOf(itemInCart.getCount()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalSum(totalSum);

        Order newOrder = orderRepository.save(order);

        cartService.clearCart();
        return newOrder.getOrderId();
    }

}