package ru.yandex.practicum.market.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.market.enums.Action;

@Getter
@Setter
public class ChangeItemQuantityInItemRequest {

    @NotNull
    private Action action;

}