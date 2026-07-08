//package ru.yandex.practicum.market.service.impl;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import ru.yandex.practicum.market.dto.ItemDto;
//import ru.yandex.practicum.market.dto.ItemsPageDto;
//import ru.yandex.practicum.market.entity.Item;
//import ru.yandex.practicum.market.enums.Action;
//import ru.yandex.practicum.market.exception.ItemNotFoundException;
//import ru.yandex.practicum.market.mapper.ItemMapper;
//import ru.yandex.practicum.market.repository.ItemRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class ItemServiceImplTest {
//
//    @Mock
//    private ItemRepository itemRepository;
//
//    @Mock
//    private ItemMapper itemMapper;
//
//    @InjectMocks
//    private ItemServiceImpl itemService;
//
//    @Test
//    void shouldReturnItemDtoById() {
//        Item item = Item.builder()
//                .itemId(1L)
//                .title("Phone")
//                .description("Desc")
//                .count(2)
//                .build();
//
//        ItemDto dto = new ItemDto(
//                1L,
//                "Phone",
//                "Desc",
//                null,
//                null,
//                2
//        );
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemMapper.toItemDto(item))
//                .thenReturn(dto);
//
//        ItemDto result = itemService.getItemDtoById(1L);
//
//        assertThat(result).isEqualTo(dto);
//
//        verify(itemRepository).findById(1L);
//        verify(itemMapper).toItemDto(item);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenItemNotFound() {
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.empty());
//
//        assertThatThrownBy(() ->
//                itemService.getItemDtoById(1L))
//                .isInstanceOf(ItemNotFoundException.class)
//                .hasMessage("Товар с id 1 не найден");
//    }
//
//    @Test
//    void shouldDecreaseItemCount() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(5)
//                .build();
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemRepository.save(any()))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        itemService.changeItemQuantityInItems(1L, Action.MINUS);
//
//        assertThat(item.getCount()).isEqualTo(4);
//    }
//
//    @Test
//    void shouldSetCountToZeroWhenCountEqualsOne() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(1)
//                .build();
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemRepository.save(any()))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        itemService.changeItemQuantityInItems(1L, Action.MINUS);
//
//        assertThat(item.getCount()).isZero();
//    }
//
//    @Test
//    void shouldLeaveZeroWhenCountAlreadyZero() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(0)
//                .build();
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemRepository.save(any()))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        itemService.changeItemQuantityInItems(1L, Action.MINUS);
//
//        assertThat(item.getCount()).isZero();
//    }
//
//    @Test
//    void shouldThrowExceptionWhenChangingMissingItem() {
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.empty());
//
//        assertThatThrownBy(() ->
//                itemService.changeItemQuantityInItems(1L, Action.PLUS))
//                .isInstanceOf(ItemNotFoundException.class);
//    }
//
//    @Test
//    void shouldReturnUpdatedItemDto() {
//
//        Item item = Item.builder()
//                .itemId(1L)
//                .count(2)
//                .build();
//
//        ItemDto dto = new ItemDto(
//                1L,
//                null,
//                null,
//                null,
//                null,
//                3
//        );
//
//        when(itemRepository.findById(1L))
//                .thenReturn(Optional.of(item));
//
//        when(itemRepository.save(any()))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        when(itemMapper.toItemDto(item))
//                .thenReturn(dto);
//
//        ItemDto result =
//                itemService.changeItemQuantityInItem(1L, Action.PLUS);
//
//        assertThat(result).isEqualTo(dto);
//    }
//
//    @Test
//    void shouldReturnItemsPageDto() {
//
//        Pageable pageable = PageRequest.of(0, 5);
//
//        Item item1 = Item.builder().itemId(1L).build();
//        Item item2 = Item.builder().itemId(2L).build();
//
//        Page<Item> page = new PageImpl<>(
//                List.of(item1, item2),
//                pageable,
//                2
//        );
//
//        when(itemRepository
//                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
//                        "", "", pageable))
//                .thenReturn(page);
//
//        when(itemMapper.toItemDto(item1))
//                .thenReturn(new ItemDto(1L, "", "", "", null, 0));
//
//        when(itemMapper.toItemDto(item2))
//                .thenReturn(new ItemDto(2L, "", "", "", null, 0));
//
//        ItemsPageDto dto = itemService.getItems("", pageable);
//
//        assertThat(dto.paging().pageNumber()).isEqualTo(1);
//        assertThat(dto.paging().pageSize()).isEqualTo(5);
//
//        assertThat(dto.items()).hasSize(1);
//        assertThat(dto.items().getFirst()).hasSize(3);
//
//        verify(itemRepository)
//                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
//                        "", "", pageable);
//    }
//
//}