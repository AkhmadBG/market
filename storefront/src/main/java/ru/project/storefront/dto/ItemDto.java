package ru.project.storefront.dto;

import java.math.BigDecimal;

public record ItemDto(

        Long itemId,

        String title,

        String description,

        String imgPath,

        BigDecimal price,

        Integer count

) {
}