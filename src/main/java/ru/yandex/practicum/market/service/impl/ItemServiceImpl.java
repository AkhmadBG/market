package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.dto.PagingDto;
import ru.yandex.practicum.market.entity.Cart;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.CartService;
import ru.yandex.practicum.market.service.ItemInCartService;
import ru.yandex.practicum.market.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemInCartService itemInCartService;
    private final CartService cartService;
    private final ItemMapper itemMapper;

    @Override
    public ItemsPageDto getItems(String search, Pageable pageable) {
        Page<Item> itemsPage = itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
        Cart cart = cartService.getActiveCart();
        Map<Long, Integer> counts = cart.getItemsInCart().stream()
                .collect(Collectors.toMap(
                        item -> item.getItem().getItemId(),
                        ItemInCart::getCount
                ));

        List<ItemDto> itemsDto = itemsPage.getContent().stream()
                .map(item -> new ItemDto(
                        item.getItemId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getImgPath(),
                        item.getPrice(),
                        counts.getOrDefault(item.getItemId(), 0)
                ))
                .toList();

        return new ItemsPageDto(
                splitByThree(itemsDto),
                new PagingDto(
                        itemsPage.getNumber() + 1,
                        itemsPage.getSize(),
                        itemsPage.hasPrevious(),
                        itemsPage.hasNext()
                ));
    }

    @Transactional
    @Override
    public void changeItemQuantityInItems(Long itemId, Action action) {
        itemInCartService.changeItemsQuantityInCart(itemId, action);
    }

    @Override
    public ItemDto getItemDtoById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден"));
        Integer itemInCartCount = itemInCartService.getItemInCartCount(itemId);
        return itemMapper.toItemDto(item, itemInCartCount);
    }

    @Transactional
    @Override
    public ItemDto changeItemQuantityInItem(Long itemId, Action action) {
        Item item = itemInCartService.changeItemsQuantityInCart(itemId, action);
        Integer itemInCartCount = itemInCartService.getItemInCartCount(itemId);
        return itemMapper.toItemDto(item, itemInCartCount);
    }

//    @Override
//    public Item getItemById(Long itemId) {
//        return itemRepository.findById(itemId)
//                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден"));
//    }

    private List<List<ItemDto>> splitByThree(List<ItemDto> items) {
        List<List<ItemDto>> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i += 3) {
            List<ItemDto> subResult = new ArrayList<>(items.subList(i, Math.min(i + 3, items.size())));
            while (subResult.size() < 3) {
                subResult.add(new ItemDto(-1L, null, null, null, null, null));
            }
            result.add(subResult);
        }
        return result;
    }

}