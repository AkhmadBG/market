package ru.project.storefront.dto;

public record PagingDto(

        Integer pageNumber,

        Integer pageSize,

        Boolean hasPrevious,

        Boolean hasNext

) {
}