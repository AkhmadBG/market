package ru.project.storefront.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.project.storefront.dto.CachedItem;
import ru.project.storefront.dto.CachedItemShort;
import ru.project.storefront.dto.CachedItems;
import ru.project.storefront.entity.Item;
import ru.project.storefront.enums.ItemSort;
import ru.project.storefront.exception.ItemNotFoundException;
import ru.project.storefront.mapper.ItemMapper;
import ru.project.storefront.repository.ItemRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ru.project.storefront.enums.ItemSort.NO;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ReactiveRedisTemplate<String, CachedItem> itemRedisTemplate;

    @Mock
    private ReactiveRedisTemplate<String, CachedItems> cachedItemsRedisTemplate;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ReactiveValueOperations<String, CachedItem> itemValueOperations;

    @Mock
    private ReactiveValueOperations<String, CachedItems> cachedItemsValueOperations;

    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(
                itemRepository,
                itemRedisTemplate,
                cachedItemsRedisTemplate,
                itemMapper
        );

        ReflectionTestUtils.setField(
                itemService,
                "cacheTtl",
                Duration.ofMinutes(5)
        );
    }

    @Test
    void getItemById_shouldLoadFromDatabaseAndSaveToRedis_whenCacheMiss() {

        Long itemId = 1L;

        Item item = createItem(itemId);

        CachedItem cachedItem = new CachedItem(
                itemId,
                "Кофе",
                "Кофе молотый",
                "/images/coffee.png",
                BigDecimal.valueOf(500)
        );

        when(itemRedisTemplate.opsForValue())
                .thenReturn(itemValueOperations);

        when(itemValueOperations.get("item:" + itemId))
                .thenReturn(Mono.empty());

        when(itemRepository.findById(itemId))
                .thenReturn(Mono.just(item));

        when(itemMapper.toCachedItem(item))
                .thenReturn(cachedItem);

        when(itemValueOperations.set("item:" + itemId, cachedItem))
                .thenReturn(Mono.just(true));

        StepVerifier.create(itemService.getItemById(itemId))
                .expectNext(item)
                .verifyComplete();

        verify(itemRepository).findById(itemId);

        verify(itemMapper).toCachedItem(item);

        verify(itemValueOperations)
                .set("item:" + itemId, cachedItem);
    }

    @Test
    void getItemById_shouldReturnError_whenItemNotFound() {

        Long itemId = 999L;

        when(itemRedisTemplate.opsForValue())
                .thenReturn(itemValueOperations);

        when(itemValueOperations.get("item:" + itemId))
                .thenReturn(Mono.empty());

        when(itemRepository.findById(itemId))
                .thenReturn(Mono.empty());

        StepVerifier.create(itemService.getItemById(itemId))
                .expectError(ItemNotFoundException.class)
                .verify();

        verify(itemRepository).findById(itemId);
    }

    @Test
    void search_shouldLoadFromDatabaseAndSaveToRedis_whenCacheMiss() {

        String search = "coffee";
        ItemSort sort = ItemSort.ALPHA;
        int pageNumber = 1;
        int pageSize = 5;

        Item item = createItem(1L);

        when(cachedItemsRedisTemplate.opsForValue())
                .thenReturn(cachedItemsValueOperations);

        when(cachedItemsValueOperations.get(
                "items:search:coffee:ALPHA:1:5"
        )).thenReturn(Mono.empty());

        when(itemRepository.countSearch(search))
                .thenReturn(Mono.just(1L));

        when(itemRepository.searchAndOrderByTitle(
                search,
                pageSize,
                0
        )).thenReturn(Flux.just(item));

        when(cachedItemsValueOperations.set(
                eq("items:search:coffee:ALPHA:1:5"),
                any(CachedItems.class),
                eq(Duration.ofMinutes(5))
        )).thenReturn(Mono.just(true));

        StepVerifier.create(
                        itemService.search(
                                search,
                                sort,
                                pageNumber,
                                pageSize
                        )
                )
                .assertNext(result -> {
                    assert result.items().size() == 1;
                    assert result.total() == 1L;
                    assert result.items().get(0).getItemId().equals(1L);
                })
                .verifyComplete();

        verify(itemRepository).countSearch(search);

        verify(itemRepository)
                .searchAndOrderByTitle(search, pageSize, 0);

        verify(cachedItemsValueOperations)
                .set(
                        eq("items:search:coffee:ALPHA:1:5"),
                        any(CachedItems.class),
                        eq(Duration.ofMinutes(5))
                );
    }

    @Test
    void search_shouldUsePriceSort() {

        String search = "coffee";

        when(cachedItemsRedisTemplate.opsForValue())
                .thenReturn(cachedItemsValueOperations);

        when(cachedItemsValueOperations.get(
                "items:search:coffee:PRICE:1:5"
        )).thenReturn(Mono.empty());

        Item item = createItem(1L);

        when(itemRepository.countSearch(search))
                .thenReturn(Mono.just(1L));

        when(itemRepository.searchAndOrderByPrice(
                search,
                5,
                0
        )).thenReturn(Flux.just(item));

        when(cachedItemsValueOperations.set(
                eq("items:search:coffee:PRICE:1:5"),
                any(CachedItems.class),
                any(Duration.class)
        )).thenReturn(Mono.just(true));

        StepVerifier.create(
                        itemService.search(
                                search,
                                ItemSort.PRICE,
                                1,
                                5
                        )
                )
                .expectNextCount(1)
                .verifyComplete();

        verify(itemRepository)
                .searchAndOrderByPrice(search, 5, 0);

        verify(itemRepository, never())
                .searchAndOrderByTitle(anyString(), anyInt(), anyLong());

        verify(itemRepository, never())
                .searchAndWithoutOrder(anyString(), anyInt(), anyLong());
    }

    @Test
    void search_shouldUseWithoutOrderForDefaultSort() {

        String search = "coffee";

        when(cachedItemsRedisTemplate.opsForValue())
                .thenReturn(cachedItemsValueOperations);

        when(cachedItemsValueOperations.get(
                "items:search:coffee:NO:1:5"
        )).thenReturn(Mono.empty());

        Item item = createItem(1L);

        when(itemRepository.countSearch(search))
                .thenReturn(Mono.just(1L));

        when(itemRepository.searchAndWithoutOrder(
                search,
                5,
                0
        )).thenReturn(Flux.just(item));

        when(cachedItemsValueOperations.set(
                anyString(),
                any(CachedItems.class),
                any(Duration.class)
        )).thenReturn(Mono.just(true));

        StepVerifier.create(
                        itemService.search(
                                search,
                                NO,
                                1,
                                5
                        )
                )
                .expectNextCount(1)
                .verifyComplete();

        verify(itemRepository)
                .searchAndWithoutOrder(search, 5, 0);
    }

    private Item createItem(Long itemId) {
        return Item.builder()
                .itemId(itemId)
                .title("Кофе")
                .description("Описание кофе")
                .imgPath("/images/coffee.png")
                .price(BigDecimal.valueOf(500))
                .build();
    }
}