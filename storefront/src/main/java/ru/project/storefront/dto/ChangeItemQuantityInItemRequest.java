package ru.project.storefront.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.project.storefront.enums.Action;

@Getter
@Setter
public class ChangeItemQuantityInItemRequest {

    @NotNull
    private Action action;

}