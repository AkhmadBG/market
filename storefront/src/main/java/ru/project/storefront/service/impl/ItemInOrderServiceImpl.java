package ru.project.storefront.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.ItemInOrder;
import ru.project.storefront.repository.ItemInOrderRepository;
import ru.project.storefront.service.ItemInOrderService;

@Service
@RequiredArgsConstructor
public class ItemInOrderServiceImpl implements ItemInOrderService {

    private final ItemInOrderRepository itemInOrderRepository;

    @Override
    public Mono<ItemInOrder> save(ItemInOrder itemInOrder) {
        return itemInOrderRepository.save(itemInOrder);
    }

    @Override
    public Flux<ItemInOrder> getItemInOrderByOrderId(Long orderId) {
        return itemInOrderRepository.findAllByOrderId(orderId);
    }

}