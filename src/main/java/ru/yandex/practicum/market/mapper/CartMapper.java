package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.entity.Cart;

import java.math.BigDecimal;
import java.util.Set;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface CartMapper {

//    @Mapping(target = "items", source = "itemsDto")
//    CartDto toCartDto(Set<ItemDto> itemsDto, BigDecimal total);

    @Mapping(target = "items", source = "cart.itemsInCart")
    @Mapping(target = "total", source = "total")
    CartDto toCartDto(Cart cart);

}