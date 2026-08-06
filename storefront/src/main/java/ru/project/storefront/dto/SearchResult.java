package ru.project.storefront.dto;

import ru.project.storefront.entity.Item;

import java.util.List;

public record SearchResult(

        List<Item> items,

        long total

) {
}
