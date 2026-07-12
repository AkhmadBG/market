package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface CartMapper {

    @Mapping(target = "items", source = "cart.itemsInCart")
    @Mapping(target = "total", source = "total")
    CartDto toCartDto(Cart cart);

}