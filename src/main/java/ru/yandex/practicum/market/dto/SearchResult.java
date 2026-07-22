package ru.yandex.practicum.market.dto;

import ru.yandex.practicum.market.entity.Item;

import java.util.List;

public record SearchResult(

        List<Item> items,

        long total

) {
}
