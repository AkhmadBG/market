package ru.yandex.practicum.market.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.yandex.practicum.market.enums.CartStatus;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "cartId")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("carts")
public class Cart {

    @Id
    @Column("cart_id")
    private Long cartId;

    @NotNull
    @PositiveOrZero
    @Column("total")
    private BigDecimal total;

    @Column("cart_status")
    private CartStatus cartStatus;

}