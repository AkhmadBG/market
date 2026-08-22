package ru.project.payment.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "accountId")
@NoArgsConstructor
@AllArgsConstructor
@Table("accounts")
public class Account {

    @Id
    @Column("account_id")
    private Long accountId;

    @NotNull
    @Column("user_id")
    private Long userId;

    @NotNull
    @PositiveOrZero
    @Column("balance")
    private BigDecimal balance;

}