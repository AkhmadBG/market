package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemInCartDto;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.entity.ItemInOrder;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toItemDto(Item item);

//    ItemInCartDto toItemInCartDto(ItemInCart item);

    @Mapping(target = "item", source = "item")
    @Mapping(target = "price", source = "item.price")
    @Mapping(target = "count", source = "item.count")
    ItemInOrder toItemInOrder(Item item);

}