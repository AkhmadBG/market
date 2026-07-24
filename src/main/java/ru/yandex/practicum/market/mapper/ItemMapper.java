package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.entity.Item;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ItemMapper {

    @Mapping(target = "count", source = "itemInCartCount")
    ItemDto toItemDto(Item item, Integer itemInCartCount);

}