package ru.project.storefront.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CartDto(

        Long cartId,

        Set<ItemDto> items,

        BigDecimal total

) {
}