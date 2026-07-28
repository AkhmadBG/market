package ru.yandex.practicum.market.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "itemInCartId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("items_in_carts")
public class ItemInCart {

    @Id
    @Column("item_in_cart_id")
    private Long itemInCartId;

    @Column("item_id")
    private Long itemId;

    @Column("cart_id")
    private Long cartId;

    @NotNull
    @PositiveOrZero
    @Column("price")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    @Column("count")
    private Integer count;

}