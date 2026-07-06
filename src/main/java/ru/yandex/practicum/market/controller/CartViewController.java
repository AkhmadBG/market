package ru.yandex.practicum.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.market.dto.CartDto;
import ru.yandex.practicum.market.dto.ItemDto;
import ru.yandex.practicum.market.enums.Action;
import ru.yandex.practicum.market.service.CartService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartViewController {

    private final CartService cartService;

    //Эндпоинт получения страницы со списком товаров в корзине
    //GET /cart/items
    @GetMapping
    public String getItemsInCart(Model model) {
        CartDto cart = cartService.getItemsInCart();
        List<ItemDto> items = cart.items().stream().toList();
        model.addAttribute("items", cart.items());
        model.addAttribute("total", cart.total());
        return "cart";
    }

    //Эндпоинт уменьшения/увеличения количества товара в корзине со страницы корзины
    //POST /cart/items?id=[id]&action=[action]
    @PostMapping
    public String changeItemsQuantityInCart(@RequestParam(name = "id") Long itemId,
                                            @RequestParam Action action,
                                            Model model) {
        CartDto cart = cartService.changeItemsQuantityInCart(itemId, action);
        model.addAttribute("items", cart.items());
        model.addAttribute("total", cart.total());
        return "cart";
    }

}


//GET /cart/items
//POST /cart/items?id=[id]&action=[action]