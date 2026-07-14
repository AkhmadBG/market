package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.ItemService;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден"));
    }

    @Override
    public Page<Item> findByTitleOrDescription(String title, String description, Pageable pageable) {
        return itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(title, description, pageable);
    }

}