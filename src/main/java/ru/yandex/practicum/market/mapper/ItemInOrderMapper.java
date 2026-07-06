package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.market.dto.ItemInOrderDto;
import ru.yandex.practicum.market.entity.ItemInOrder;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface ItemInOrderMapper {

    @Mapping(target = "itemDto", source = "item")
    ItemInOrderDto toItemInOrderDto(ItemInOrder itemInorder);

}
