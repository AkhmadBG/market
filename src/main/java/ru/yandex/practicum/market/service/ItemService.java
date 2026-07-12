package ru.yandex.practicum.market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.market.entity.Item;

public interface ItemService {

    Item getItemById(Long itemId);

    Page<Item> findByTitleOrDescription(String title, String description, Pageable pageable);

}