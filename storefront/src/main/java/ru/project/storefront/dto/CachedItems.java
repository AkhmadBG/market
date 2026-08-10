package ru.project.storefront.dto;

import java.util.List;

public record CachedItems(

        List<CachedItemShort> items,

        Long total

) {
}