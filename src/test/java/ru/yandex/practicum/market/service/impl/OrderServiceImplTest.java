//package ru.yandex.practicum.market.service.impl;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.yandex.practicum.market.dto.OrderDto;
//import ru.yandex.practicum.market.entity.Item;
//import ru.yandex.practicum.market.entity.ItemInOrder;
//import ru.yandex.practicum.market.entity.Order;
//import ru.yandex.practicum.market.exception.OrderNotFoundException;
//import ru.yandex.practicum.market.mapper.ItemMapper;
//import ru.yandex.practicum.market.mapper.OrderMapper;
//import ru.yandex.practicum.market.repository.ItemInOrderRepository;
//import ru.yandex.practicum.market.repository.ItemRepository;
//import ru.yandex.practicum.market.repository.OrderRepository;
//import ru.yandex.practicum.market.service.CartService;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceImplTest {
//
//    @Mock
//    private ItemInOrderRepository itemInOrderRepository;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private ItemRepository itemRepository;
//
//    @Mock
//    private CartService cartService;
//
//    @Mock
//    private OrderMapper orderMapper;
//
//    @Mock
//    private ItemMapper itemMapper;
//
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    @Test
//    void shouldReturnOrders() {
//
//        Order order1 = Order.builder().orderId(1L).build();
//        Order order2 = Order.builder().orderId(2L).build();
//
//        OrderDto dto1 = mock(OrderDto.class);
//        OrderDto dto2 = mock(OrderDto.class);
//
//        when(orderRepository.findAll())
//                .thenReturn(List.of(order1, order2));
//
//        when(orderMapper.toOrderDto(order1))
//                .thenReturn(dto1);
//
//        when(orderMapper.toOrderDto(order2))
//                .thenReturn(dto2);
//
//        List<OrderDto> result = orderService.getOrders();
//
//        assertThat(result)
//                .containsExactly(dto1, dto2);
//
//        verify(orderRepository).findAll();
//        verify(orderMapper).toOrderDto(order1);
//        verify(orderMapper).toOrderDto(order2);
//    }
//
//    @Test
//    void shouldReturnOrderById() {
//
//        Order order = Order.builder()
//                .orderId(10L)
//                .build();
//
//        OrderDto dto = mock(OrderDto.class);
//
//        when(orderRepository.findById(10L))
//                .thenReturn(Optional.of(order));
//
//        when(orderMapper.toOrderDto(order))
//                .thenReturn(dto);
//
//        OrderDto result = orderService.getOrderById(10L, false);
//
//        assertThat(result).isSameAs(dto);
//
//        verify(orderRepository).findById(10L);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenOrderNotFound() {
//
//        when(orderRepository.findById(5L))
//                .thenReturn(Optional.empty());
//
//        assertThatThrownBy(() ->
//                orderService.getOrderById(5L, false))
//                .isInstanceOf(OrderNotFoundException.class)
//                .hasMessage("Заказ с id = 5 не найден");
//    }
//
//    @Test
//    void shouldCreateNewOrder() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .price(BigDecimal.valueOf(100))
//                .count(2)
//                .build();
//
//        ItemInOrder itemInOrder = ItemInOrder.builder().build();
//
//        Order savedOrder = Order.builder()
//                .orderId(15L)
//                .build();
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(Set.of(item));
//
//        when(itemMapper.toItemInOrder(item))
//                .thenReturn(itemInOrder);
//
//        when(itemInOrderRepository.save(itemInOrder))
//                .thenReturn(itemInOrder);
//
//        when(orderRepository.save(any(Order.class)))
//                .thenReturn(savedOrder);
//
//        Long result = orderService.newOrder();
//
//        assertThat(result).isEqualTo(15L);
//
//        verify(itemRepository).findByCountGreaterThan(0);
//        verify(itemMapper).toItemInOrder(item);
//        verify(itemInOrderRepository).save(itemInOrder);
//        verify(orderRepository).save(any(Order.class));
//        verify(cartService).deleteCart();
//    }
//
//    @Test
//    void shouldCalculateTotalCorrectly() {
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
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(Set.of(item1, item2));
//
//        when(itemMapper.toItemInOrder(any()))
//                .thenReturn(ItemInOrder.builder().build());
//
//        when(itemInOrderRepository.save(any()))
//                .thenAnswer(inv -> inv.getArgument(0));
//
//        ArgumentCaptor<Order> captor =
//                ArgumentCaptor.forClass(Order.class);
//
//        when(orderRepository.save(any()))
//                .thenAnswer(inv -> {
//                    Order order = inv.getArgument(0);
//                    order.setOrderId(1L);
//                    return order;
//                });
//
//        orderService.newOrder();
//
//        verify(orderRepository).save(captor.capture());
//
//        assertThat(captor.getValue().getTotalSum())
//                .isEqualByComparingTo("350");
//    }
//
//    @Test
//    void shouldSetOrderForEveryItem() {
//
//        Item item = Item.builder()
//                .price(BigDecimal.TEN)
//                .count(1)
//                .build();
//
//        ItemInOrder itemInOrder =
//                ItemInOrder.builder().build();
//
//        Order savedOrder = Order.builder()
//                .orderId(10L)
//                .build();
//
//        when(itemRepository.findByCountGreaterThan(0))
//                .thenReturn(Set.of(item));
//
//        when(itemMapper.toItemInOrder(item))
//                .thenReturn(itemInOrder);
//
//        when(itemInOrderRepository.save(itemInOrder))
//                .thenReturn(itemInOrder);
//
//        when(orderRepository.save(any()))
//                .thenReturn(savedOrder);
//
//        orderService.newOrder();
//
//        assertThat(itemInOrder.getOrder())
//                .isSameAs(savedOrder);
//    }
//
//}