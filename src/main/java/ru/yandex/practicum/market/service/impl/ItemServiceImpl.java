package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.SearchResult;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.enums.ItemSort;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.ItemService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Mono<Item> getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .switchIfEmpty(
                        Mono.error(new ItemNotFoundException("Товар с id " + itemId + " не найден"))
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