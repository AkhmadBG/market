//package ru.yandex.practicum.market.service.impl;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.yandex.practicum.market.dto.CartDto;
//import ru.yandex.practicum.market.entity.Item;
//import ru.yandex.practicum.market.enums.Action;
//import ru.yandex.practicum.market.exception.ItemNotFoundException;
//import ru.yandex.practicum.market.mapper.CartMapper;
//import ru.yandex.practicum.market.repository.ItemRepository;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CartServiceImplTest {
//
//    @Mock
//    private ItemRepository itemRepository;
//
//    @Mock
//    private CartMapper cartMapper;
//
//    @InjectMocks
//    private CartServiceImpl cartService;
//
//    @Test
//    void shouldReturnCartDto() {
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(2)
//                .price(BigDecimal.valueOf(100))
//                .build();
//
//        Set<Item> items = Set.of(item);
//
//        CartDto cartDto = mock(CartDto.class);
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(items);
//
//        when(cartMapper.toCartDto(items, BigDecimal.valueOf(200)))
//                .thenReturn(cartDto);
//
//        CartDto result = cartService.getItemsInCart();
//
//        assertThat(result).isSameAs(cartDto);
//
//        verify(cartMapper).toCartDto(items, BigDecimal.valueOf(200));
//    }
//
//    @Test
//    void shouldIncreaseItemCount() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(2)
//                .price(BigDecimal.TEN)
//                .build();
//
//        CartDto cartDto = mock(CartDto.class);
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemRepository.save(any()))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(Set.of(item));
//
//        when(cartMapper.toCartDto(anySet(), any()))
//                .thenReturn(cartDto);
//
//        CartDto result =
//                cartService.changeItemsQuantityInCart(1L, Action.PLUS);
//
//        assertThat(item.getCount()).isEqualTo(3);
//        assertThat(result).isSameAs(cartDto);
//
//        verify(itemRepository).save(item);
//    }
//
//    @Test
//    void shouldDecreaseItemCount() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(5)
//                .price(BigDecimal.TEN)
//                .build();
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemRepository.save(any()))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(Set.of(item));
//
//        when(cartMapper.toCartDto(anySet(), any()))
//                .thenReturn(mock(CartDto.class));
//
//        cartService.changeItemsQuantityInCart(1L, Action.MINUS);
//
//        assertThat(item.getCount()).isEqualTo(4);
//    }
//
//    @Test
//    void shouldSetCountToZeroWhenMinusAndCountEqualsOne() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(1)
//                .price(BigDecimal.TEN)
//                .build();
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemRepository.save(any()))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(Set.of());
//
//        when(cartMapper.toCartDto(anySet(), any()))
//                .thenReturn(mock(CartDto.class));
//
//        cartService.changeItemsQuantityInCart(1L, Action.MINUS);
//
//        assertThat(item.getCount()).isZero();
//    }
//
//    @Test
//    void shouldDeleteItemFromCart() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(5)
//                .price(BigDecimal.TEN)
//                .build();
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemRepository.save(any()))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(Set.of());
//
//        when(cartMapper.toCartDto(anySet(), any()))
//                .thenReturn(mock(CartDto.class));
//
//        cartService.changeItemsQuantityInCart(1L, Action.DELETE);
//
//        assertThat(item.getCount()).isZero();
//
//        verify(itemRepository).save(item);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenItemNotFound() {
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.empty());
//
//        assertThatThrownBy(() ->
//                cartService.changeItemsQuantityInCart(1L, Action.PLUS))
//                .isInstanceOf(ItemNotFoundException.class)
//                .hasMessage("Товар с id 1 не найден в корзине");
//    }
//
//    @Test
//    void shouldCalculateTotalSum() {
//
//        Item item1 = Item.builder()
//                .itemId(1L)
//                .price(BigDecimal.valueOf(100))
//                .count(2)
//                .build();
//
//        Item item2 = Item.builder()
//                .itemId(2L)
//                .price(BigDecimal.valueOf(50))
//                .count(3)
//                .build();
//
//        Set<Item> items = Set.of(item1, item2);
//
//        CartDto cartDto = mock(CartDto.class);
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(items);
//
//        when(cartMapper.toCartDto(items, BigDecimal.valueOf(350)))
//                .thenReturn(cartDto);
//
//        CartDto result = cartService.getCartDto();
//
//        assertThat(result).isSameAs(cartDto);
//
//        verify(cartMapper)
//                .toCartDto(items, BigDecimal.valueOf(350));
//    }
//
//    @Test
//    void shouldDeleteAllItemsFromCart() {
//
//        Item item1 = Item.builder().itemId(1L).count(3).build();
//        Item item2 = Item.builder().itemId(2L).count(5).build();
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(Set.of(item1, item2));
//
//        cartService.deleteCart();
//
//        assertThat(item1.getCount()).isZero();
//        assertThat(item2.getCount()).isZero();
//    }
//
//}