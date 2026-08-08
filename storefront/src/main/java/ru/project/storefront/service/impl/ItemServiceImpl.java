package ru.project.storefront.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.project.storefront.dto.CachedItem;
import ru.project.storefront.dto.SearchResult;
import ru.project.storefront.entity.Item;
import ru.project.storefront.enums.ItemSort;
import ru.project.storefront.exception.ItemNotFoundException;
import ru.project.storefront.mapper.ItemMapper;
import ru.project.storefront.repository.ItemRepository;
import ru.project.storefront.service.ItemService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ReactiveRedisTemplate<String, CachedItem> itemRedisTemplate;
    private final ItemMapper itemMapper;

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
    public Mono<SearchResult> search(String search, ItemSort itemSort, int pageNumber, int pageSize) {

        long offset = (long) (pageNumber - 1) * pageSize;

        int limit = pageSize;

        Mono<Long> total = itemRepository.countSearch(search);
        Mono<List<Item>> items;
        switch (itemSort) {
            case ALPHA -> items = itemRepository.searchAndOrderByTitle(search, limit, offset).collectList();
            case PRICE -> items = itemRepository.searchAndOrderByPrice(search, limit, offset).collectList();
            default -> items = itemRepository.searchAndWithoutOrder(search, limit, offset).collectList();
        }

        return Mono.zip(items, total)
                .map(tuple -> new SearchResult(tuple.getT1(), tuple.getT2()));
    }

}