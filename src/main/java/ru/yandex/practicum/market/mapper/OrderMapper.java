package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.market.dto.ItemInOrderDto;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.ItemInOrder;
import ru.yandex.practicum.market.entity.Order;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = "spring", uses = ItemInOrderMapper.class)
public interface OrderMapper {

    @Mapping(target = "items", source = "itemsInOrder")
    OrderDto toOrderDto(Order order);

}