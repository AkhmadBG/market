package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.ItemService;

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
    public Flux<Item> findByTitleOrDescription(String title, String description, Pageable pageable) {
        return itemRepository.findByTitleOrDescription(title, description, pageable);
    }

}