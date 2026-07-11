package ru.yandex.practicum.market.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.enums.Action;

public interface ItemService {

    ItemsPageDto getItems(String search, Pageable pageable);

    void changeItemQuantityInItems(Long itemId, Action action);

    ItemDto getItemDtoById(Long itemId);

    ItemDto changeItemQuantityInItem(Long itemId, Action action);

    Item getItemById(Long itemId);

}