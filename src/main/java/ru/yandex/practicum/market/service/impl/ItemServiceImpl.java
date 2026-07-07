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
import ru.yandex.practicum.market.repository.CartRepository;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final ItemInCartRepository itemInCartRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemsPageDto getItems(String search, Pageable pageable) {
        Page<Item> itemsPage = itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
        Cart cart = cartRepository.findAll().getFirst();
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
        changeItemQuantity(itemId, action);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден"));
        Optional<ItemInCart> itemInCartOpt = itemInCartRepository.findByItem_ItemId(itemId);
        ItemDto itemDto = itemMapper.toItemDto(item);
        if (itemInCartOpt.isPresent()) {
            itemDto.count().
        }
        return
    }

    @Transactional
    @Override
    public ItemDto changeItemQuantityInItem(Long itemId, Action action) {
        Item item = changeItemQuantity(itemId, action);
        return itemMapper.toItemDto(item);
    }

    @Transactional
    protected Item changeItemQuantity(Long itemId, Action action) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден"));
        switch (action) {
            case PLUS -> item.setCount(item.getCount() + 1);
            case MINUS -> {
                if (item.getCount() == 0 || item.getCount() == 1) {
                    item.setCount(0);
                } else {
                    item.setCount(item.getCount() - 1);
                }
            }
            default -> throw new IllegalStateException("Значения " + action + " нет в enum Action");
        }

        return itemRepository.save(item);
    }

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