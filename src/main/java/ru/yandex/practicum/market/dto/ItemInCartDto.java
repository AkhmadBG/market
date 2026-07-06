package ru.yandex.practicum.market.dto;

public record ItemInCartDto(

        Long itemInCartId,

        ItemDto itemDto,

        Integer count

) {
}