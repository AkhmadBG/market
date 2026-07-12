package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.market.entity.ItemInOrder;
import ru.yandex.practicum.market.repository.ItemInOrderRepository;
import ru.yandex.practicum.market.service.ItemInOrderService;

@Service
@RequiredArgsConstructor
public class ItemInOrderServiceImpl implements ItemInOrderService {

    private final ItemInOrderRepository itemInOrderRepository;

    @Override
    public ItemInOrder save(ItemInOrder itemInOrder) {
        return itemInOrderRepository.save(itemInOrder);
    }

}