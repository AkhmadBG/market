package ru.yandex.practicum.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemInCartDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.ItemSort;
import ru.yandex.practicum.market.service.ItemService;

@Controller
@RequiredArgsConstructor
public class ItemViewController {

    private final ItemService itemService;

    //Эндпоинт получения товаров на странице или
    //GET /?search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]
    @GetMapping("/")
    public String redirectToItems() {
        return "redirect:/items";
    }

    //Эндпоинт получения товаров на странице
    //GET /items?search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]
    @GetMapping("/items")
    public String getItems(@RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "NO") ItemSort itemSort,
                           @RequestParam(defaultValue = "1") int pageNumber,
                           @RequestParam(defaultValue = "5") int pageSize,
                           Model model) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, getSort(itemSort));
        ItemsPageDto itemsPageDto = itemService.getItems(search, pageable);
        model.addAttribute("items", itemsPageDto.items());
        model.addAttribute("paging", itemsPageDto.paging());
        model.addAttribute("search", search);
        model.addAttribute("itemSort", itemSort);
        return "items";
    }

    //Эндпоинт уменьшения/увеличения количества товара в корзине со страницы товаров в корзине
    //POST /items?id=[id]&search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]&action=[action]
    @PostMapping("/items")
    public String changeItemQuantityInItems(@RequestParam(name = "id") Long itemId,
                                            @RequestParam(required = false, defaultValue = "") String search,
                                            @RequestParam(required = false, defaultValue = "NO") ItemSort itemSort,
                                            @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                            @RequestParam(required = false, defaultValue = "5") int pageSize,
                                            @RequestParam Action action) {
        itemService.changeItemQuantityInItems(itemId, action);
        return "redirect:/items?search=" + search + "&sort=" + itemSort + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }

    //Эндпоинт получения страницы с товаром
    //GET /items/{id}
    @GetMapping("/items/{id}")
    public String getItemById(@PathVariable(name = "id") Long itemId, Model model) {
        ItemDto item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        return "item";
    }

    //Эндпоинт уменьшения/увеличения количества товара в корзине со страницы товара в корзине
    //POST /items/{id}?action=[action]
    @PostMapping("/items/{id}")
    public String changeItemQuantityInItem(@PathVariable(name = "id") Long itemId,
                                           @RequestParam Action action,
                                           Model model) {
        ItemInCartDto item = itemService.changeItemQuantityInItem(itemId, action);
        model.addAttribute("item", item);
        return "item";
    }

    private Sort getSort(ItemSort itemSort) {
        return switch (itemSort) {
            case NO -> Sort.unsorted();
            case ALPHA -> Sort.by("title");
            case PRICE -> Sort.by("price");
        };
    }

}


//GET /items/{id}
//GET /?search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]
//GET /items?search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]
//POST /items/{id}?action=[action]
//POST /items?id=[id]&search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]&action=[action]