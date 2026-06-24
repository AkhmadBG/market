package ru.yandex.practicum.market.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "itemInOrderId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items_in_orders")
public class ItemInOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_in_order_id")
    private Long itemInOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull
    @Positive
    @Column(name = "count", nullable = false)
    private Integer count;

}