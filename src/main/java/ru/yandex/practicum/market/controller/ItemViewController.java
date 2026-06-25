package ru.yandex.practicum.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemInCartDto;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.Sort;
import ru.yandex.practicum.market.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/items", "/"})
public class ItemViewController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public String getItemById(@PathVariable Long itemId, Model model) {
        ItemDto item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        return "item";
    }

    @GetMapping
    public String getItems(@RequestParam(required = false, defaultValue = "") String search,
                           @RequestParam(required = false, defaultValue = "NO") String sort,
                           @RequestParam(required = false, defaultValue = "1") int pageNumber,
                           @RequestParam(required = false, defaultValue = "5") int pageSize,
                           Model model) {
        Sort checkSort = Sort.checkSortFromString(sort);
        List<ItemInCartDto> items = itemService.getItems(search, checkSort, pageNumber, pageSize);
        model.addAttribute("items", items);
        return "items";
    }

    @PostMapping("/{itemId}")
    public String changeItemQuantityInItem(@PathVariable Long itemId,
                                           @RequestParam String action,
                                           Model model) {
        Action checkAction = Action.checkActionFromString(action);
        ItemInCartDto item = itemService.changeItemQuantityInItem(itemId, checkAction);
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping
    public String changeItemQuantityInItems(@RequestParam Long itemId,
                                            @RequestParam(required = false, defaultValue = "") String search,
                                            @RequestParam(required = false, defaultValue = "NO") String sort,
                                            @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                            @RequestParam(required = false, defaultValue = "5") int pageSize,
                                            @RequestParam String action,
                                            Model model) {
        Sort checkSort = Sort.checkSortFromString(sort);
        Action checkAction = Action.checkActionFromString(action);
        ItemInCartDto item = itemService.changeItemQuantityInItems(itemId, search, checkSort, pageNumber, pageSize, checkAction);
        model.addAttribute("item", item);
        return "item";
    }

}


//GET /items/{id}
//GET /?search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]
//GET /items?search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]
//POST /items/{id}?action=[action]
//POST /items?id=[id]&search=[search]&sort=[sort]&pageNumber=[pageNumber]&pageSize=[pageSize]&action=[action]