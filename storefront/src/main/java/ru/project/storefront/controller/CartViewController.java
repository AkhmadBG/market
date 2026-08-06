package ru.project.storefront.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.project.storefront.dto.ChangeItemQuantityInCartRequest;
import ru.project.storefront.service.MarketService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartViewController {

    private final MarketService marketService;

    @GetMapping
    public Mono<String> getItemsInCart(Model model) {
        return marketService.getActiveCartDto()
                .map(cartDto -> {
                    model.addAttribute("items", cartDto.items());
                    model.addAttribute("total", cartDto.total());
                    return "cart";
                });
    }

    @PostMapping
    public Mono<String> changeItemsQuantityInCart(@Valid @ModelAttribute ChangeItemQuantityInCartRequest request) {
        return marketService.changeItemQuantityInCart(request.getId(), request.getAction())
                .thenReturn("redirect:/cart/items");
    }

}