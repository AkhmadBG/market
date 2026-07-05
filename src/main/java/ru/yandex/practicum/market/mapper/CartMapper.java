package ru.yandex.practicum.market.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toCartDto(Cart saveCart);

}