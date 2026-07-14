package ru.yandex.practicum.market.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInOrder;
import ru.yandex.practicum.market.repository.ItemInOrderRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemInOrderServiceImplTest {

    @Mock
    private ItemInOrderRepository itemInOrderRepository;

    @InjectMocks
    private ItemInOrderServiceImpl itemInOrderService;

    private ItemInOrder itemInOrder;

    @BeforeEach
    void setUp() {
        Item item = Item.builder()
                .itemId(1L)
                .title("Товар")
                .price(BigDecimal.valueOf(100))
                .build();

        itemInOrder = ItemInOrder.builder()
                .itemInOrderId(1L)
                .item(item)
                .count(2)
                .price(BigDecimal.valueOf(200))
                .build();
    }

    @Test
    void save_shouldReturnSavedItemInOrder() {
        when(itemInOrderRepository.save(itemInOrder))
                .thenReturn(itemInOrder);

        ItemInOrder result = itemInOrderService.save(itemInOrder);

        assertEquals(itemInOrder, result);

        verify(itemInOrderRepository).save(itemInOrder);
        verifyNoMoreInteractions(itemInOrderRepository);
    }

}