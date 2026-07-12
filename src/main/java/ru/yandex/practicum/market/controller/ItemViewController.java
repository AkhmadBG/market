package ru.yandex.practicum.market.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.dto.ItemsPageDto;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.ItemSort;
import ru.yandex.practicum.market.service.MarketService;

@Controller
@RequiredArgsConstructor
@Validated
public class ItemViewController {

    private final MarketService marketService;

    @GetMapping("/")
    public String redirectToItems() {
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String getItems(@RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "NO") ItemSort itemSort,
                           @RequestParam(defaultValue = "1") @Min(1) int pageNumber,
                           @RequestParam(defaultValue = "5") @Min(1) @Max(100) int pageSize,
                           Model model) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, getSort(itemSort));
        ItemsPageDto itemsPageDto = marketService.getItems(search, pageable);
        model.addAttribute("items", itemsPageDto.items());
        model.addAttribute("paging", itemsPageDto.paging());
        model.addAttribute("search", search);
        model.addAttribute("itemSort", itemSort);
        return "items";
    }

    @PostMapping("/items")
    public String changeItemQuantityInItems(@RequestParam(name = "id") Long itemId,
                                            @RequestParam(required = false, defaultValue = "") String search,
                                            @RequestParam(required = false, defaultValue = "NO") ItemSort itemSort,
                                            @RequestParam(required = false, defaultValue = "1") @Min(1) int pageNumber,
                                            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(100) int pageSize,
                                            @RequestParam Action action,
                                            RedirectAttributes redirectAttributes) {
        marketService.changeItemsQuantityInCart(itemId, action);
        redirectAttributes.addAttribute("search", search);
        redirectAttributes.addAttribute("itemSort", itemSort);
        redirectAttributes.addAttribute("pageNumber", pageNumber);
        redirectAttributes.addAttribute("pageSize", pageSize);
        return "redirect:/items";
    }

    @GetMapping("/items/{id}")
    public String getItemById(@PathVariable(name = "id") Long itemId, Model model) {
        ItemDto item = marketService.getItemDtoById(itemId);
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping("/items/{id}")
    public String changeItemQuantityInItem(@PathVariable(name = "id") Long itemId,
                                           @RequestParam Action action,
                                           Model model) {
        ItemDto item = marketService.changeItemQuantityInItem(itemId, action);
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