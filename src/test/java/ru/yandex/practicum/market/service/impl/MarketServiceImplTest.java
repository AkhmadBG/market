package ru.yandex.practicum.market.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.entity.*;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.repository.CartRepository;
import ru.yandex.practicum.market.repository.ItemInCartRepository;
import ru.yandex.practicum.market.repository.ItemInOrderRepository;
import ru.yandex.practicum.market.repository.ItemRepository;
import ru.yandex.practicum.market.service.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketServiceImplTest {

    @Mock
    private ItemService itemService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @Mock
    private ItemInCartService itemInCartService;

    @Mock
    private ItemInOrderService itemInOrderService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private MarketServiceImpl marketService;

    @Test
    void getItems_shouldReturnItemsWithCountFromCart() {
        Pageable pageable = PageRequest.of(0, 5);

        Item item = Item.builder()
                .itemId(1L)
                .title("Item")
                .description("Description")
                .price(BigDecimal.TEN)
                .build();

        Page<Item> page = new PageImpl<>(List.of(item), pageable, 1);

        ItemInCart itemInCart = ItemInCart.builder()
                .item(item)
                .count(3)
                .build();

        Cart cart = Cart.builder()
                .itemsInCart(new HashSet<>(Set.of(itemInCart)))
                .build();

        when(itemService.findByTitleOrDescription("", "", pageable))
                .thenReturn(page);
        when(cartService.getActiveCart())
                .thenReturn(cart);

        ItemsPageDto result = marketService.getItems("", pageable);

        assertEquals(1, result.items().size());
        assertEquals(3, result.items().getFirst().getFirst().count());

        verify(itemService).findByTitleOrDescription("", "", pageable);
        verify(cartService).getActiveCart();
    }

    @Test
    void getItemDtoById_shouldReturnDtoWithCount() {

        Item item = Item.builder()
                .itemId(1L)
                .price(BigDecimal.TEN)
                .build();

        ItemDto dto = new ItemDto(1L, "", "", "", BigDecimal.TEN, 5);

        when(itemService.getItemById(1L)).thenReturn(item);

        when(itemInCartService.findByCart_CartStatusAndItem_ItemId(
                CartStatus.ACTIVE, 1L))
                .thenReturn(Optional.of(
                        ItemInCart.builder()
                                .count(5)
                                .build()));

        when(itemMapper.toItemDto(item, 5))
                .thenReturn(dto);

        ItemDto result = marketService.getItemDtoById(1L);

        assertEquals(dto, result);
    }

    @Test
    void getItemInCartCount_shouldReturnZero() {

        when(itemInCartService.findByCart_CartStatusAndItem_ItemId(
                CartStatus.ACTIVE, 1L))
                .thenReturn(Optional.empty());

        Integer result = marketService.getItemInCartCount(1L);

        assertEquals(0, result);
    }

    @Test
    void changeItemQuantityInCart_shouldIncreaseCount() {

        Item item = Item.builder()
                .itemId(1L)
                .price(BigDecimal.TEN)
                .build();

        ItemInCart itemInCart = ItemInCart.builder()
                .item(item)
                .count(1)
                .build();

        Cart cart = Cart.builder()
                .itemsInCart(new HashSet<>(Set.of(itemInCart)))
                .build();

        when(cartService.getActiveCart()).thenReturn(cart);
        when(itemService.getItemById(1L)).thenReturn(item);

        marketService.changeItemQuantityInCart(1L, Action.PLUS);

        assertEquals(2, itemInCart.getCount());
        assertEquals(BigDecimal.valueOf(20), cart.getTotal());

        verify(itemInCartService, atLeastOnce()).save(itemInCart);
    }

    @Test
    void changeItemQuantityInCart_shouldDecreaseCount() {

        Item item = Item.builder()
                .itemId(1L)
                .price(BigDecimal.TEN)
                .build();

        ItemInCart itemInCart = ItemInCart.builder()
                .item(item)
                .count(2)
                .build();

        Cart cart = Cart.builder()
                .itemsInCart(new HashSet<>(Set.of(itemInCart)))
                .build();

        when(cartService.getActiveCart()).thenReturn(cart);
        when(itemService.getItemById(1L)).thenReturn(item);

        marketService.changeItemQuantityInCart(1L, Action.MINUS);

        assertEquals(1, itemInCart.getCount());
    }

    @Test
    void changeItemQuantityInCart_shouldRemoveItem() {

        Item item = Item.builder()
                .itemId(1L)
                .price(BigDecimal.TEN)
                .build();

        ItemInCart itemInCart = ItemInCart.builder()
                .item(item)
                .count(1)
                .build();

        Set<ItemInCart> items = new HashSet<>();
        items.add(itemInCart);

        Cart cart = Cart.builder()
                .itemsInCart(items)
                .build();

        when(cartService.getActiveCart()).thenReturn(cart);
        when(itemService.getItemById(1L)).thenReturn(item);

        marketService.changeItemQuantityInCart(1L, Action.DELETE);

        assertTrue(cart.getItemsInCart().isEmpty());
    }

    @Test
    void createOrder_shouldCreateOrder() {

        Item item = Item.builder()
                .itemId(1L)
                .price(BigDecimal.TEN)
                .build();

        ItemInCart itemInCart = ItemInCart.builder()
                .item(item)
                .count(2)
                .build();

        Cart cart = Cart.builder()
                .itemsInCart(new HashSet<>(Set.of(itemInCart)))
                .total(BigDecimal.valueOf(20))
                .build();

        ItemInOrder itemInOrder = ItemInOrder.builder().build();

        Order savedOrder = new Order();
        savedOrder.setOrderId(100L);

        when(cartService.getActiveCart()).thenReturn(cart);
        when(itemMapper.toItemInOrder(itemInCart)).thenReturn(itemInOrder);
        when(orderService.save(any(Order.class))).thenReturn(savedOrder);

        Long result = marketService.createOrder();

        assertEquals(100L, result);

        verify(cartService).closeCart();
        verify(orderService).save(any(Order.class));
    }

}