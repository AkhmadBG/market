package ru.yandex.practicum.market.dto;

public record ItemInOrderDto(

        Long itemInOrderDto,

        ItemDto itemDto,

        OrderDto orderDto,

        Integer count

) {
}
