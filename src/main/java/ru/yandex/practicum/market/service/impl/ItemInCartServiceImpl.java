package ru.yandex.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.service.ItemInCartService;

@Service
@RequiredArgsConstructor
public class ItemInCartServiceImpl implements ItemInCartService {

    private final ItemInCartRepository ItemInCartRepository;

}