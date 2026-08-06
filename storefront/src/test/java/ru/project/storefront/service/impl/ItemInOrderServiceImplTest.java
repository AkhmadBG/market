package ru.project.storefront.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.market.entity.ItemInOrder;
import ru.yandex.practicum.market.repository.ItemInOrderRepository;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemInOrderServiceImplTest {

    @Mock
    private ItemInOrderRepository itemInOrderRepository;

    @InjectMocks
    private ItemInOrderServiceImpl itemInOrderService;

    @Test
    void shouldSaveItemInOrder() {
        ItemInOrder itemInOrder = new ItemInOrder(
                1L,
                10L,
                100L,
                BigDecimal.valueOf(250),
                2
        );

        when(itemInOrderRepository.save(itemInOrder))
                .thenReturn(Mono.just(itemInOrder));

        StepVerifier.create(itemInOrderService.save(itemInOrder))
                .expectNext(itemInOrder)
                .verifyComplete();

        verify(itemInOrderRepository).save(itemInOrder);
        verifyNoMoreInteractions(itemInOrderRepository);
    }

    @Test
    void shouldReturnItemsByOrderId() {
        ItemInOrder first = new ItemInOrder(
                1L,
                10L,
                100L,
                BigDecimal.valueOf(250),
                2
        );

        ItemInOrder second = new ItemInOrder(
                2L,
                20L,
                100L,
                BigDecimal.valueOf(500),
                1
        );

        when(itemInOrderRepository.findAllByOrderId(100L))
                .thenReturn(Flux.just(first, second));

        StepVerifier.create(itemInOrderService.getItemInOrderByOrderId(100L))
                .expectNext(first)
                .expectNext(second)
                .verifyComplete();

        verify(itemInOrderRepository).findAllByOrderId(100L);
        verifyNoMoreInteractions(itemInOrderRepository);
    }
}