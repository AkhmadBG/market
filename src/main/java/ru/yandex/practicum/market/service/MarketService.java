package ru.yandex.practicum.market.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.enums.Action;

public interface MarketService {

    ItemsPageDto getItems(String search, Pageable pageable);

    ItemDto getItemDtoById(Long itemId);

    Integer getItemInCartCount(Long itemId);

    ItemDto changeItemQuantityInItem(Long itemId, Action action);

    void changeItemsQuantityInCart(Long itemId, Action action);

    Long newOrder();

}