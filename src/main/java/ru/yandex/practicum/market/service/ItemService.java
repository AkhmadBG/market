package ru.yandex.practicum.market.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.SearchResult;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.enums.ItemSort;

public interface ItemService {

    Mono<Item> getItemById(Long itemId);

    Mono<SearchResult> search(String search, ItemSort itemSort, int pageNumber, int pageSize);

}