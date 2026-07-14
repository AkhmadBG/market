package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;

@Mapper(componentModel = "spring", uses = ItemMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CartMapper {

    @Mapping(target = "items", source = "cart.itemsInCart")
    @Mapping(target = "total", source = "total")
    CartDto toCartDto(Cart cart);

}