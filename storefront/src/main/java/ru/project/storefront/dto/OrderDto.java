package ru.project.storefront.dto;

import java.math.BigDecimal;
import java.util.Set;

public record OrderDto(

        Long orderId,

        Set<ItemInOrderDto> items,

        BigDecimal totalSum

) {
}