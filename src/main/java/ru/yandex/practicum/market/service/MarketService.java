package ru.yandex.practicum.market.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.enums.Action;

public interface MarketService {

    void changeItemsQuantityInCart(Long itemId, Action action);

    Integer getItemInCartCount(Long itemId);

    Long newOrder();

    ItemsPageDto getItems(String search, Pageable pageable);

    ItemDto changeItemQuantityInItem(Long itemId, Action action);

    ItemDto getItemDtoById(Long itemId);

}