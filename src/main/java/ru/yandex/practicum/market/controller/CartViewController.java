package ru.yandex.practicum.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.service.CartService;
import ru.yandex.practicum.market.service.MarketService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartViewController {

    private final CartService cartService;
    private final MarketService marketService;

    @GetMapping
    public Mono<String> getItemsInCart(Model model) {
        return cartService.getActiveCartDto()
                        .map(cartDto -> {
                            model.addAttribute("items", cartDto.items());
                            model.addAttribute("total", cartDto.total());
                            return "cart";
                        });
    }

    @PostMapping
    public Mono<String> changeItemsQuantityInCart(@RequestParam(name = "id") Long itemId,
                                            @RequestParam Action action) {
        return marketService.changeItemQuantityInCart(itemId, action)
                .thenReturn("redirect:/cart/items");
    }

}