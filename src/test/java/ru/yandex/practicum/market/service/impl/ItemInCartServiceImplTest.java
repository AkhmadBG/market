package ru.yandex.practicum.market.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.repository.ItemInCartRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemInCartServiceImplTest {

    @Mock
    private ItemInCartRepository itemInCartRepository;

    @InjectMocks
    private ItemInCartServiceImpl itemInCartService;

    private ItemInCart itemInCart;

    @BeforeEach
    void setUp() {
        Item item = Item.builder()
                .itemId(1L)
                .title("Товар")
                .price(BigDecimal.valueOf(100))
                .build();

        itemInCart = ItemInCart.builder()
                .itemInCartId(1L)
                .item(item)
                .count(2)
                .price(BigDecimal.valueOf(200))
                .build();
    }

    @Test
    void findByCartStatusAndItemId_shouldReturnItemInCart() {
        when(itemInCartRepository.findByCart_CartStatusAndItem_ItemId(CartStatus.ACTIVE, 1L))
                .thenReturn(Optional.of(itemInCart));

        Optional<ItemInCart> result =
                itemInCartService.findByCart_CartStatusAndItem_ItemId(CartStatus.ACTIVE, 1L);

        assertEquals(Optional.of(itemInCart), result);

        verify(itemInCartRepository)
                .findByCart_CartStatusAndItem_ItemId(CartStatus.ACTIVE, 1L);
    }

    @Test
    void save_shouldCallRepository() {
        itemInCartService.save(itemInCart);

        verify(itemInCartRepository).save(itemInCart);
    }

}