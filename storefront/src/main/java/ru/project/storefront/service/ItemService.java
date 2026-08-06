package ru.project.storefront.service;

import reactor.core.publisher.Mono;
import ru.project.storefront.dto.SearchResult;
import ru.project.storefront.entity.Item;
import ru.project.storefront.enums.ItemSort;

public interface ItemService {

    Mono<Item> getItemById(Long itemId);

    Mono<SearchResult> search(String search, ItemSort itemSort, int pageNumber, int pageSize);

}