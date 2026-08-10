package ru.project.storefront.dto;

import java.math.BigDecimal;

public record CachedItemShort(

        Long itemId,

        String title,

        String description,

        BigDecimal price

) {
}