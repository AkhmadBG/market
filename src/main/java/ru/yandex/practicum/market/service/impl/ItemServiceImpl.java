package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.ItemsPageDto;
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
    private final DatabaseClient databaseClient;

    @Override
    public Mono<Item> getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .switchIfEmpty(
                        Mono.error(new ItemNotFoundException("Товар с id " + itemId + " не найден"))
                );
    }

    @Override
    public Mono<SearchResult> search(String search, ItemSort itemSort, int pageNumber, int pageSize) {

        int limit = pageSize;

        long offset = pageable.getOffset();

        Mono<Long> total =
                itemRepository.countSearch(search);

        Mono<List<Item>> items =
                itemRepository.search(search, limit, offset)
                        .collectList();

        return Mono.zip(items, total)
                .map(tuple -> {

                    List<Item> content = tuple.getT1();

                    Long totalElements = tuple.getT2();

                    return ItemsPageDto.builder()
                            .items(content)
                            .totalItems(totalElements)
                            .page(pageable.getPageNumber())
                            .size(pageable.getPageSize())
                            .build();
                });
    }

}