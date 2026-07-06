package ru.yandex.practicum.market.dto;

import java.math.BigDecimal;

public record ItemInOrderDto(

        Long itemInOrderDto,

        ItemDto itemDto,

        OrderDto orderDto,

        BigDecimal price,

        Integer count

) {
}