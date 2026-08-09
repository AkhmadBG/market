package ru.project.storefront.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.project.storefront.entity.Item;
import ru.project.storefront.enums.ItemSort;
import ru.project.storefront.exception.ItemNotFoundException;
import ru.project.storefront.repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void shouldSearchWithoutOrder() {
        List<Item> items = List.of(
                Item.builder().itemId(1L).title("A").build(),
                Item.builder().itemId(2L).title("B").build()
        );

        when(itemRepository.countSearch("phone"))
                .thenReturn(Mono.just(2L));

        when(itemRepository.searchAndWithoutOrder("phone", 10, 0))
                .thenReturn(Flux.fromIterable(items));

        StepVerifier.create(itemService.search("phone", ItemSort.NO, 1, 10))
                .assertNext(result -> {
                    assertEquals(2L, result.total());
                    assertEquals(items, result.items());
                })
                .verifyComplete();

        verify(itemRepository).searchAndWithoutOrder("phone", 10, 0);
        verify(itemRepository).countSearch("phone");
    }

    @Test
    void shouldSearchOrderedByTitle() {
        List<Item> items = List.of(
                Item.builder().itemId(1L).title("A").build()
        );

        when(itemRepository.countSearch("phone"))
                .thenReturn(Mono.just(1L));

        when(itemRepository.searchAndOrderByTitle("phone", 10, 0))
                .thenReturn(Flux.fromIterable(items));

        StepVerifier.create(itemService.search("phone", ItemSort.ALPHA, 1, 10))
                .assertNext(result -> {
                    assertEquals(1L, result.total());
                    assertEquals(items, result.items());
                })
                .verifyComplete();

        verify(itemRepository).searchAndOrderByTitle("phone", 10, 0);
    }

    @Test
    void shouldSearchOrderedByPrice() {
        List<Item> items = List.of(
                Item.builder().itemId(1L).title("Phone").build()
        );

        when(itemRepository.countSearch("phone"))
                .thenReturn(Mono.just(1L));

        when(itemRepository.searchAndOrderByPrice("phone", 10, 0))
                .thenReturn(Flux.fromIterable(items));

        StepVerifier.create(itemService.search("phone", ItemSort.PRICE, 1, 10))
                .assertNext(result -> {
                    assertEquals(1L, result.total());
                    assertEquals(items, result.items());
                })
                .verifyComplete();

        verify(itemRepository).searchAndOrderByPrice("phone", 10, 0);
    }

    @Test
    void shouldCalculateOffsetCorrectly() {
        when(itemRepository.countSearch(""))
                .thenReturn(Mono.just(0L));

        when(itemRepository.searchAndOrderByPrice("", 20, 40))
                .thenReturn(Flux.empty());

        StepVerifier.create(itemService.search("", ItemSort.PRICE, 3, 20))
                .assertNext(result -> {
                    assertEquals(0, result.items().size());
                    assertEquals(0L, result.total());
                })
                .verifyComplete();

        verify(itemRepository).searchAndOrderByPrice("", 20, 40);
    }

}