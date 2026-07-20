package ru.yandex.practicum.market.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.Item;

public interface ItemService {

    Mono<Item> getItemById(Long itemId);

    Flux<Item> findByTitleOrDescription(String title, String description, Pageable pageable);

}