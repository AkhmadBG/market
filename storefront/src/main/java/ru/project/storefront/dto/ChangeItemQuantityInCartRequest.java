package ru.project.storefront.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.project.storefront.enums.Action;

@Setter
@Getter
public class ChangeItemQuantityInCartRequest {

    @NotNull
    private Long id;

    @NotNull
    private Action action;

}