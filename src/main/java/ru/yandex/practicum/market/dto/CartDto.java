package ru.yandex.practicum.market.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CartDto(

        Set<ItemDto> items,

        BigDecimal total

) {
}