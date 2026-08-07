package ru.project.storefront.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto{

    private Long cartId;

    private Set<ItemDto> items;

    private BigDecimal total;

    private boolean canOrder;

    private String paymentMessage;

    public CartDto(Long cartId, Set<ItemDto> items, BigDecimal total) {

    }
}