package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemInCartDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.dto.PagingDto;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.ItemSort;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemInCartRepository itemInCartRepository;

    @Override
    public ItemsPageDto getItems(String search, Pageable pageable) {
        Page<Item> itemsPage = itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
        List<ItemDto> items = itemsPage.getContent().stream()
                .map(itemMapper::toItemDto)
                .toList();
        return new ItemsPageDto(
                splitByThree(items),
                new PagingDto(
                        itemsPage.getNumber() + 1,
                        itemsPage.getSize(),
                        itemsPage.hasPrevious(),
                        itemsPage.hasNext()
                ));
    }

    @Override
    public void changeItemQuantityInItems(Long itemId, Action action) {
        changeItemQuantity(itemId, action);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден"));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemInCartDto changeItemQuantityInItem(Long itemId, Action action) {
        ItemInCart itemInCart = changeItemQuantity(itemId, action);
        return itemMapper.toItemInCartDto(itemInCart);
    }

    @Transactional
    protected ItemInCart changeItemQuantity(Long itemId, Action action) {
        ItemInCart itemInCart = itemInCartRepository.findByItem_ItemId(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден в корзине"));
        switch (action) {
            case PLUS -> itemInCart.setCount(itemInCart.getCount() + 1);
            case MINUS -> {
                if (itemInCart.getCount() == 1) {
                    itemInCartRepository.deleteById(itemId);
                } else {
                    itemInCart.setCount(itemInCart.getCount() - 1);
                }
            }
            default -> throw new IllegalStateException("Значения " + action + " нет в enum Action");
        }
        return itemInCartRepository.save(itemInCart);
    }

    private List<List<ItemDto>> splitByThree(List<ItemDto> items) {
        List<List<ItemDto>> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i += 3) {
            List<ItemDto> subResult = new ArrayList<>(items.subList(i, Math.min(i + 3, items.size())));
            while (subResult.size() < 3) {
                subResult.add(new ItemDto(-1L, null, null, null, null));
            }
            result.add(subResult);
        }
        return result;
    }

}