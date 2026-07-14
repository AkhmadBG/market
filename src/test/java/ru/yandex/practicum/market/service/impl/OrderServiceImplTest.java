package ru.yandex.practicum.market.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Order;
import ru.yandex.practicum.market.exception.OrderNotFoundException;
import ru.yandex.practicum.market.mapper.OrderMapper;
import ru.yandex.practicum.market.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getOrders_shouldReturnOrderDtos() {
        Order order1 = Order.builder()
                .orderId(1L)
                .totalSum(BigDecimal.valueOf(100))
                .build();

        Order order2 = Order.builder()
                .orderId(2L)
                .totalSum(BigDecimal.valueOf(200))
                .build();

        OrderDto dto1 = mock(OrderDto.class);
        OrderDto dto2 = mock(OrderDto.class);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        when(orderMapper.toOrderDto(order1)).thenReturn(dto1);
        when(orderMapper.toOrderDto(order2)).thenReturn(dto2);

        List<OrderDto> result = orderService.getOrders();

        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));

        verify(orderRepository).findAll();
        verify(orderMapper).toOrderDto(order1);
        verify(orderMapper).toOrderDto(order2);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    void getOrderById_shouldReturnOrderDto() {
        Order order = Order.builder()
                .orderId(1L)
                .totalSum(BigDecimal.valueOf(150))
                .build();

        OrderDto dto = mock(OrderDto.class);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderDto(order)).thenReturn(dto);

        OrderDto result = orderService.getOrderById(1L);

        assertSame(dto, result);

        verify(orderRepository).findById(1L);
        verify(orderMapper).toOrderDto(order);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    void getOrderById_shouldThrowException_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(1L)
        );

        assertEquals("Заказ с id = 1 не найден", exception.getMessage());

        verify(orderRepository).findById(1L);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    void save_shouldReturnSavedOrder() {
        Order order = Order.builder()
                .orderId(1L)
                .totalSum(BigDecimal.valueOf(300))
                .build();

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.save(order);

        assertSame(order, result);

        verify(orderRepository).save(order);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

}