package ru.yandex.practicum.market.dto;

public record PagingDto(

        Integer pageNumber,

        Integer pageSize,

        Boolean hasPrevious,

        Boolean hasNext

) {
}