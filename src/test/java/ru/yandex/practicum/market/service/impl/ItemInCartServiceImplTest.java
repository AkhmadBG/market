package ru.yandex.practicum.market.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.repository.ItemInCartRepository;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemInCartServiceImplTest {

    @Mock
    private ItemInCartRepository repository;

    @Mock
    private DatabaseClient databaseClient;

    @InjectMocks
    private ItemInCartServiceImpl service;

    @Test
    void shouldFindAllByCartId() {

        ItemInCart item1 = new ItemInCart(1L, 10L, 100L, BigDecimal.TEN, 2);
        ItemInCart item2 = new ItemInCart(2L, 20L, 100L, BigDecimal.ONE, 5);

        when(repository.findAllByCartId(100L))
                .thenReturn(Flux.just(item1, item2));

        StepVerifier.create(service.findAllByCartId(100L))
                .expectNext(item1)
                .expectNext(item2)
                .verifyComplete();

        verify(repository).findAllByCartId(100L);
    }

    @Test
    void shouldSaveItemInCart() {

        ItemInCart item = new ItemInCart(
                1L,
                10L,
                100L,
                BigDecimal.TEN,
                2
        );

        when(repository.save(item))
                .thenReturn(Mono.just(item));

        StepVerifier.create(service.save(item))
                .expectNext(item)
                .verifyComplete();

        verify(repository).save(item);
    }

    @Test
    void shouldDeleteItemInCart() {

        when(repository.deleteById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.delete(1L))
                .verifyComplete();

        verify(repository).deleteById(1L);
    }

    @Test
    void shouldFindByCartIdAndItemId() {

        ItemInCart item = new ItemInCart(
                1L,
                10L,
                100L,
                BigDecimal.TEN,
                2
        );

        when(repository.findByCartIdAndItemId(100L, 10L))
                .thenReturn(Mono.just(item));

        StepVerifier.create(service.findByCartIdAndItemId(100L, 10L))
                .expectNext(item)
                .verifyComplete();

        verify(repository).findByCartIdAndItemId(100L, 10L);
    }
}