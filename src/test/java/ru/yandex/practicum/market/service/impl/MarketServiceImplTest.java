package ru.yandex.practicum.market.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.entity.*;
import ru.yandex.practicum.market.enums.CartStatus;
import ru.yandex.practicum.market.exception.OrderNotFoundException;
import ru.yandex.practicum.market.mapper.ItemMapper;
import ru.yandex.practicum.market.service.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void shouldReturnItemCountInCart() {
        ItemInCart itemInCart = ItemInCart.builder()
                .count(3)
                .build();

        when(itemInCartService.findByCartStatusAndItemId(CartStatus.ACTIVE, 1L))
                .thenReturn(Mono.just(itemInCart));

        StepVerifier.create(marketService.getItemInCartCount(1L))
                .expectNext(3)
                .verifyComplete();

        verify(itemInCartService).findByCartStatusAndItemId(CartStatus.ACTIVE, 1L);
    }

    @Test
    void shouldReturnZeroWhenItemNotInCart() {

        when(itemInCartService.findByCartStatusAndItemId(CartStatus.ACTIVE, 1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(marketService.getItemInCartCount(1L))
                .expectNext(0)
                .verifyComplete();

        verify(itemInCartService).findByCartStatusAndItemId(CartStatus.ACTIVE, 1L);
    }

    @Test
    void shouldReturnActiveCartDto() {

        Cart cart = Cart.builder()
                .cartId(1L)
                .total(BigDecimal.TEN)
                .build();

        Item item = Item.builder()
                .itemId(5L)
                .title("Phone")
                .price(BigDecimal.TEN)
                .build();

        ItemInCart itemInCart = ItemInCart.builder()
                .cartId(1L)
                .itemId(5L)
                .count(2)
                .build();

        ItemDto itemDto = new ItemDto(
                5L,
                "Phone",
                null,
                null,
                BigDecimal.TEN,
                2
        );

        when(cartService.getOrCreateActiveCart())
                .thenReturn(Mono.just(cart));

        when(itemInCartService.findAllByCartId(1L))
                .thenReturn(Flux.just(itemInCart));

        when(itemService.getItemById(5L))
                .thenReturn(Mono.just(item));

        when(itemMapper.toItemDto(item, 2))
                .thenReturn(itemDto);

        StepVerifier.create(marketService.getActiveCartDto())
                .assertNext(dto -> {
                    assertEquals(1L, dto.cartId());
                    assertEquals(BigDecimal.TEN, dto.total());
                    assertEquals(1, dto.items().size());
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnOrderById() {

        Order order = Order.builder()
                .orderId(1L)
                .totalSum(BigDecimal.valueOf(200))
                .build();

        Item item = Item.builder()
                .itemId(10L)
                .title("Phone")
                .price(BigDecimal.valueOf(200))
                .build();

        ItemInOrder itemInOrder = new ItemInOrder(
                100L,
                10L,
                1L,
                BigDecimal.valueOf(200),
                2
        );

        when(orderService.getOrderById(1L))
                .thenReturn(Mono.just(order));

        when(itemInOrderService.getItemInOrderByOrderId(1L))
                .thenReturn(Flux.just(itemInOrder));

        when(itemService.getItemById(10L))
                .thenReturn(Mono.just(item));

        StepVerifier.create(marketService.getOrderById(1L))
                .assertNext(dto -> {
                    assertEquals(1L, dto.orderId());
                    assertEquals(BigDecimal.valueOf(200), dto.totalSum());
                    assertEquals(1, dto.items().size());
                })
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {

        when(orderService.getOrderById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(marketService.getOrderById(1L))
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    @Test
    void shouldReturnItemDtoById() {

        Item item = Item.builder()
                .itemId(1L)
                .title("Phone")
                .price(BigDecimal.valueOf(100))
                .build();

        ItemDto itemDto = new ItemDto(
                1L,
                "Phone",
                null,
                null,
                BigDecimal.valueOf(100),
                2
        );

        ItemInCart itemInCart = ItemInCart.builder()
                .count(2)
                .build();

        when(itemService.getItemById(1L))
                .thenReturn(Mono.just(item));

        when(itemInCartService.findByCartStatusAndItemId(CartStatus.ACTIVE, 1L))
                .thenReturn(Mono.just(itemInCart));

        when(itemMapper.toItemDto(item, 2))
                .thenReturn(itemDto);

        StepVerifier.create(marketService.getItemDtoById(1L))
                .expectNext(itemDto)
                .verifyComplete();

        verify(itemMapper).toItemDto(item, 2);
    }
}