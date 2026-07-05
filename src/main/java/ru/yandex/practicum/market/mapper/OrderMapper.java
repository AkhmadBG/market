package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

}