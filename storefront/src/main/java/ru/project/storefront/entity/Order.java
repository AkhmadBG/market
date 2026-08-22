package ru.project.storefront.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "orderId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class Order {

    @Id
    @Column("order_id")
    private Long orderId;

    @Column("user_id")
    private Long userId;

    @NotNull
    @PositiveOrZero
    @Column("total_sum")
    private BigDecimal totalSum;

}