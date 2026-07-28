package ru.yandex.practicum.market.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "itemInOrderId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("items_in_orders")
public class ItemInOrder {

    @Id
    @Column("item_in_order_id")
    private Long itemInOrderId;

    @Column("item_id")
    private Long itemId;

    @Column("order_id")
    private Long orderId;

    @NotNull
    @Positive
    @Column("price")
    private BigDecimal price;

    @NotNull
    @Positive
    @Column("count")
    private Integer count;

}