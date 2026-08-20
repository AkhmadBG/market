package ru.project.storefront.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {

    @Id
    @Column("user_id")
    private Long userId;

    @NotBlank
    @Column("username")
    private String username;

    @NotBlank
    @Column("password")
    private String password;

}