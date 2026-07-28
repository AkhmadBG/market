package ru.yandex.practicum.market.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "itemId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("items")
public class Item {

    @Id
    @Column("item_id")
    private Long itemId;

    @NotBlank
    @Column("title")
    private String title;

    @NotBlank
    @Column("description")
    private String description;

    @Column("img_path")
    private String imgPath;

    @NotNull
    @Positive
    @Column("price")
    private BigDecimal price;

}