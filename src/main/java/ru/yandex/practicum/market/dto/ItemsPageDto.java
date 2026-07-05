package ru.yandex.practicum.market.dto;

import java.util.List;

public record ItemsPageDto(

        List<List<ItemDto>> items,

        PagingDto paging

) {
}
