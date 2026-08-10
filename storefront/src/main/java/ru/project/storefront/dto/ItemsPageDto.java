package ru.project.storefront.dto;

import java.util.List;

public record ItemsPageDto(

        List<List<ItemDto>> items,

        PagingDto paging

) {
}