package ru.project.storefront.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.project.storefront.dto.ChangeItemQuantityInItemRequest;
import ru.project.storefront.dto.ChangeItemQuantityInItemsRequest;
import ru.project.storefront.enums.ItemSort;
import ru.project.storefront.service.MarketService;

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
    public Mono<String> changeItemQuantityInItems(@Valid @ModelAttribute ChangeItemQuantityInItemsRequest request) {
        String redirect = UriComponentsBuilder
                .fromPath("/items")
                .queryParam("search", request.getSearch())
                .queryParam("itemSort", request.getItemSort())
                .queryParam("pageNumber", request.getPageNumber())
                .queryParam("pageSize", request.getPageSize())
                .build()
                .toUriString();
        return marketService.changeItemQuantityInCart(
                        request.getId(),
                        request.getAction())
                .thenReturn("redirect:" + redirect);
    }


    @GetMapping("/items/{id}")
    public Mono<String> getItemById(@PathVariable(name = "id") Long itemId,
                                    Model model) {
        return marketService.getItemDtoById(itemId)
                .map(itemDto -> {
                    model.addAttribute("item", itemDto);
                    return "item";
                });
    }

    @PostMapping("/items/{id}")
    public Mono<String> changeItemQuantityInItem(@PathVariable(name = "id") Long itemId,
                                                 @Valid @ModelAttribute ChangeItemQuantityInItemRequest request,
                                                 Model model) {
        return marketService.changeItemQuantityInCart(itemId, request.getAction())
                .map(itemDto -> {
                    model.addAttribute("item", itemDto);
                    return "item";
                });
    }

}