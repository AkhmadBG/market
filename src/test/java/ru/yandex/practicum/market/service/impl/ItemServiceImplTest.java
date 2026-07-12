package ru.yandex.practicum.market.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.exception.ItemNotFoundException;
import ru.yandex.practicum.market.repository.ItemRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getItemById_shouldReturnItem_whenItemExists() {
        Item item = Item.builder()
                .itemId(1L)
                .title("Ноутбук")
                .description("Описание")
                .price(BigDecimal.valueOf(100000))
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item result = itemService.getItemById(1L);

        assertEquals(item, result);

        verify(itemRepository).findById(1L);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getItemById_shouldThrowException_whenItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> itemService.getItemById(1L)
        );

        assertEquals("Товар с id 1 не найден", exception.getMessage());

        verify(itemRepository).findById(1L);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void findByTitleOrDescription_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 5);

        Item item = Item.builder()
                .itemId(1L)
                .title("Ноутбук")
                .description("Описание")
                .price(BigDecimal.valueOf(100000))
                .build();

        Page<Item> expectedPage = new PageImpl<>(List.of(item), pageable, 1);

        when(itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "ноут",
                "ноут",
                pageable
        )).thenReturn(expectedPage);

        Page<Item> result = itemService.findByTitleOrDescription(
                "ноут",
                "ноут",
                pageable
        );

        assertEquals(expectedPage, result);
        assertEquals(1, result.getTotalElements());
        assertEquals(item, result.getContent().getFirst());

        verify(itemRepository)
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "ноут",
                        "ноут",
                        pageable
                );
        verifyNoMoreInteractions(itemRepository);
    }

}