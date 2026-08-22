package ru.project.storefront.dto;

import java.math.BigDecimal;

public record CachedItemShort(

        Long itemId,

        String title,

        String description,

        String imgPath,

        BigDecimal price

) {
}