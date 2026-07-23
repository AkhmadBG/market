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
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.enums.ItemSort;
import ru.yandex.practicum.market.service.MarketService;

@Controller
@RequiredArgsConstructor
@Validated
public class ItemViewController {

    private final MarketService marketService;

    @GetMapping("/")
    public Mono<String> redirectToItems() {
        return Mono.just("redirect:/items");
    }

    @GetMapping("/items")
    public Mono<String> getItems(@RequestParam(defaultValue = "") String search,
                                 @RequestParam(defaultValue = "NO") ItemSort itemSort,
                                 @RequestParam(defaultValue = "1") @Min(1) int pageNumber,
                                 @RequestParam(defaultValue = "5") @Min(1) @Max(100) int pageSize,
                                 Model model) {

        return marketService.getItems(search, itemSort, pageNumber, pageSize)
                .map(itemsPageDto -> {
                    model.addAttribute("items", itemsPageDto.items());
                    model.addAttribute("paging", itemsPageDto.paging());
                    model.addAttribute("search", search);
                    model.addAttribute("itemSort", itemSort);
                    return "items";
                });
    }

    @PostMapping("/items")
    public Mono<String> changeItemQuantityInItems(@RequestParam(name = "id") Long itemId,
                                                  @RequestParam(required = false, defaultValue = "") String search,
                                                  @RequestParam(required = false, defaultValue = "NO") ItemSort itemSort,
                                                  @RequestParam(required = false, defaultValue = "1") @Min(1) int pageNumber,
                                                  @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(100) int pageSize,
                                                  @RequestParam Action action) {
        String redirect = UriComponentsBuilder
                .fromPath("/items")
                .queryParam("search", search)
                .queryParam("itemSort", itemSort)
                .queryParam("pageNumber", pageNumber)
                .queryParam("pageSize", pageSize)
                .build()
                .toUriString();
        return marketService.changeItemQuantityInCart(itemId, action)
                .thenReturn("redirect:" + redirect);
    }

    @GetMapping("/items/{id}")
    public Mono<String> getItemById(@PathVariable(name = "id") Long itemId, Model model) {
        return marketService.getItemDtoById(itemId)
                .map(itemDto -> {
                    model.addAttribute("item", itemDto);
                    return "item";
                });
    }

    @PostMapping("/items/{id}")
    public Mono<String> changeItemQuantityInItem(@PathVariable(name = "id") Long itemId,
                                                 @RequestParam Action action,
                                                 Model model) {
        return marketService.changeItemQuantityInItem(itemId, action)
                .map(itemDto -> {
                    model.addAttribute("item", itemDto);
                    return "item";
                });
    }

    private Sort getSort(ItemSort itemSort) {
        return switch (itemSort) {
            case NO -> Sort.unsorted();
            case ALPHA -> Sort.by("title");
            case PRICE -> Sort.by("price");
        };
    }

}