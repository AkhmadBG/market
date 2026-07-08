package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.entity.ItemInOrder;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toItemDto(Item item, Integer itemInCartCount);

    @Mapping(target = "item", source = "item")
    @Mapping(target = "price", source = "itemInCart.price")
    @Mapping(target = "count", source = "itemInCart.count")
    ItemInOrder toItemInOrder(ItemInCart itemInCart);

}