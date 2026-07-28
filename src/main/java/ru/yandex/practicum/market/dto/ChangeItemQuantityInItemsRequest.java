package ru.yandex.practicum.market.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.ItemSort;

@Getter
@Setter
public class ChangeItemQuantityInItemsRequest {

    @NotNull
    private Long id;

    private String search = "";

    private ItemSort itemSort = ItemSort.NO;

    @Min(1)
    private int pageNumber = 1;

    @Min(1)
    @Max(100)
    private int pageSize = 5;

    @NotNull
    private Action action;

}