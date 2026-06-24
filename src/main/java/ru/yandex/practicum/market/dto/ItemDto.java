package ru.yandex.practicum.market.dto;

import java.math.BigDecimal;

public record ItemDto(

        Long itemId,

        String title,

        String description,

        String imgPath,

        BigDecimal price

) {
}