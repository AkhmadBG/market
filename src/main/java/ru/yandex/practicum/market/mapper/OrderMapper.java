package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Order;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "items", source = "itemsInOrder")
    OrderDto toOrderDto(Order order);

}