package ru.project.storefront.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.project.storefront.entity.Order;
import ru.project.storefront.repository.OrderRepository;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldReturnAllOrders() {
        Order order1 = Order.builder()
                .orderId(1L)
                .totalSum(BigDecimal.valueOf(100))
                .build();

        Order order2 = Order.builder()
                .orderId(2L)
                .totalSum(BigDecimal.valueOf(200))
                .build();

        when(orderRepository.findAll())
                .thenReturn(Flux.just(order1, order2));

        StepVerifier.create(orderService.getOrders())
                .expectNext(order1)
                .expectNext(order2)
                .verifyComplete();

        verify(orderRepository).findAll();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldSaveOrder() {
        Order order = Order.builder()
                .totalSum(BigDecimal.valueOf(100))
                .build();

        Order savedOrder = Order.builder()
                .orderId(1L)
                .totalSum(BigDecimal.valueOf(100))
                .build();

        when(orderRepository.save(order))
                .thenReturn(Mono.just(savedOrder));

        StepVerifier.create(orderService.save(order))
                .expectNext(savedOrder)
                .verifyComplete();

        verify(orderRepository).save(order);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnOrderById() {
        Order order = Order.builder()
                .orderId(1L)
                .totalSum(BigDecimal.valueOf(100))
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Mono.just(order));

        StepVerifier.create(orderService.getOrderById(1L))
                .expectNext(order)
                .verifyComplete();

        verify(orderRepository).findById(1L);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        when(orderRepository.findById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(orderService.getOrderById(1L))
                .verifyComplete();

        verify(orderRepository).findById(1L);
        verifyNoMoreInteractions(orderRepository);
    }
}