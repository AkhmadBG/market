package ru.project.storefront.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.project.storefront.dto.CachedItem;
import ru.project.storefront.dto.ItemDto;
import ru.project.storefront.entity.Item;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ItemMapper {

    @Mapping(target = "count", source = "itemInCartCount")
    ItemDto toItemDto(Item item, Integer itemInCartCount);

    @Mapping(target = "count", ignore = true)
    CachedItem toCachedItem(Item item);

    Item toItem(CachedItem cachedItem);

}