package ru.project.storefront.dto;

import java.math.BigDecimal;

public record ItemInOrderDto(

        Long itemInOrderId,

        ItemDto itemDto,

        BigDecimal price,

        Integer count

) {
}