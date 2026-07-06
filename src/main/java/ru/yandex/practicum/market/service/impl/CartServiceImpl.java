package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.mapper.CartMapper;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.CartService;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ItemRepository itemRepository;
    private final CartMapper cartMapper;

    @Transactional
    @Override
    public CartDto getItemsInCart() {
        return getCartDto();
    }

    @Transactional
    @Override
    public CartDto changeItemsQuantityInCart(Long itemId, Action action) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не найден в корзине"));
        switch (action) {
            case PLUS -> {
                item.setCount(item.getCount() + 1);
                itemRepository.save(item);
            }
            case MINUS -> {
                if (item.getCount() == 1 || item.getCount() == 0) {
                    item.setCount(0);
                    itemRepository.save(item);
                } else {
                    item.setCount(item.getCount() - 1);
                    itemRepository.save(item);
                }
            }
            case DELETE -> {
                item.setCount(0);
                itemRepository.save(item);
            }
            default -> throw new IllegalStateException("Значения " + action + " нет в enum Action");
        }
        return getCartDto();
    }

    @Transactional
    @Override
    public CartDto getCartDto() {
        Set<Item> items = itemRepository.findByCountGreaterThan(0);
        BigDecimal total = items.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return cartMapper.toCartDto(items, total);
    }

    @Transactional
    @Override
    public void deleteCart() {
        itemRepository.findByCountGreaterThan(0).forEach(
                item -> item.setCount(0));
    }

}