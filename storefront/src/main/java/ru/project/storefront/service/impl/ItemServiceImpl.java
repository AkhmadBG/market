package ru.project.storefront.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.project.storefront.dto.CachedItem;
import ru.project.storefront.dto.CachedItemShort;
import ru.project.storefront.dto.CachedItems;
import ru.project.storefront.dto.SearchResult;
import ru.project.storefront.entity.Item;
import ru.project.storefront.enums.ItemSort;
import ru.project.storefront.exception.ItemNotFoundException;
import ru.project.storefront.mapper.ItemMapper;
import ru.project.storefront.repository.ItemRepository;
import ru.project.storefront.service.ItemService;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ReactiveRedisTemplate<String, CachedItem> itemRedisTemplate;
    private final ReactiveRedisTemplate<String, CachedItems> cachedItemsRedisTemplate;
    private final ItemMapper itemMapper;
    @Value("${cache.ttl}")
    private Duration cacheTtl;

    @Override
    public Mono<Item> getItemById(Long itemId) {
        return itemRedisTemplate.opsForValue()
                .get("item:" + itemId)
                .map(itemMapper::toItem)
                .switchIfEmpty(
                        itemRepository.findById(itemId)
                                .switchIfEmpty(
                                        Mono.error(new ItemNotFoundException("Товар с id " + itemId + " не найден"))
                                )
                                .flatMap(item -> {
                                    CachedItem cachedItem = itemMapper.toCachedItem(item);
                                    return itemRedisTemplate.opsForValue()
                                            .set("item:" + itemId, cachedItem)
                                            .thenReturn(item);
                                })
                );
    }

    @Override
    public Mono<SearchResult> search(
            String search,
            ItemSort itemSort,
            int pageNumber,
            int pageSize) {

        String cacheKey = createSearchCacheKey(
                search,
                itemSort,
                pageNumber,
                pageSize
        );

        return cachedItemsRedisTemplate.opsForValue()
                .get(cacheKey)
                .map(this::toSearchResult)
                .switchIfEmpty(
                        searchFromDatabase(
                                search,
                                itemSort,
                                pageNumber,
                                pageSize
                        )
                                .flatMap(result -> saveToCache(cacheKey, result))
                );
    }

    private String createSearchCacheKey(
            String search,
            ItemSort itemSort,
            int pageNumber,
            int pageSize) {

        return String.format(
                "items:search:%s:%s:%d:%d",
                search == null ? "" : search,
                itemSort,
                pageNumber,
                pageSize
        );
    }

    private Mono<SearchResult> searchFromDatabase(
            String search,
            ItemSort itemSort,
            int pageNumber,
            int pageSize) {

        long offset = (long) (pageNumber - 1) * pageSize;

        Mono<Long> total = itemRepository.countSearch(search);

        Mono<List<Item>> items;

        switch (itemSort) {
            case ALPHA -> items =
                    itemRepository
                            .searchAndOrderByTitle(search, pageSize, offset)
                            .collectList();

            case PRICE -> items =
                    itemRepository
                            .searchAndOrderByPrice(search, pageSize, offset)
                            .collectList();

            default -> items =
                    itemRepository
                            .searchAndWithoutOrder(search, pageSize, offset)
                            .collectList();
        }

        return Mono.zip(items, total)
                .map(tuple -> new SearchResult(
                        tuple.getT1(),
                        tuple.getT2()
                ));
    }

    private Mono<SearchResult> saveToCache(
            String cacheKey,
            SearchResult result) {

        CachedItems cachedItems = new CachedItems(
                result.items().stream()
                        .map(item -> new CachedItemShort(
                                item.getItemId(),
                                item.getTitle(),
                                item.getDescription(),
                                item.getPrice()
                        ))
                        .toList(),
                result.total()
        );

        return cachedItemsRedisTemplate
                .opsForValue()
                .set(cacheKey, cachedItems, cacheTtl)
                .thenReturn(result);
    }

    private SearchResult toSearchResult(CachedItems cachedItems) {

        List<Item> items = cachedItems.items()
                .stream()
                .map(item -> Item.builder()
                        .itemId(item.itemId())
                        .title(item.title())
                        .description(item.description())
                        .price(item.price())
                        .build())
                .toList();

        return new SearchResult(
                items,
                cachedItems.total()
        );
    }

}