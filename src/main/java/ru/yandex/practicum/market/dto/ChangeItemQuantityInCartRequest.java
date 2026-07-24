package ru.yandex.practicum.market.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.market.enums.Action;

@Setter
@Getter
public class ChangeItemQuantityInCartRequest {

    @NotNull
    private Long id;

    @NotNull
    private Action action;

}