package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemInOrderDto;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.entity.ItemInOrder;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "count", source = "itemInCartCount")
//    @Mapping(target = "price", source = "total")
    ItemDto toItemDto(Item item, Integer itemInCartCount);

    @Mapping(target = "item", source = "item")
    @Mapping(target = "price", source = "itemInCart.price")
    @Mapping(target = "count", source = "itemInCart.count")
    ItemInOrder toItemInOrder(ItemInCart itemInCart);

    @Mapping(target = "itemDto", source = "item")
    ItemInOrderDto toItemInOrderDto(ItemInOrder itemInorder);

    @Mapping(target = "itemId", source = "itemInCart.item.itemId")
    @Mapping(target = "title", source = "itemInCart.item.title")
    @Mapping(target = "description", source = "itemInCart.item.description")
    @Mapping(target = "imgPath", source = "itemInCart.item.imgPath")
    @Mapping(target = "price", source = "itemInCart.price")
    @Mapping(target = "count", source = "itemInCart.count")
    ItemDto toItemDtoFromItemInCart(ItemInCart itemInCart);

}