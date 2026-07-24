package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.entity.ItemInOrder;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.repository.ItemInOrderRepository;
import ru.yandex.practicum.market.service.ItemInOrderService;

@Service
@RequiredArgsConstructor
public class ItemInOrderServiceImpl implements ItemInOrderService {

    private final ItemInOrderRepository itemInOrderRepository;
    private final ItemMapper itemMapper;

    @Override
    public Mono<ItemInOrder> save(ItemInOrder itemInOrder) {
        return itemInOrderRepository.save(itemInOrder);
    }

    @Override
    public Flux<ItemInOrder> getItemInOrderByOrderId(Long orderId) {
        return itemInOrderRepository.findAllByOrderId(orderId);
    }

}