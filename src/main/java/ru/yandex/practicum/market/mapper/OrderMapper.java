package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.entity.Order;

@Mapper(componentModel = "spring", uses = ItemMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderMapper {


}