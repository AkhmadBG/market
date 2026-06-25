package ru.yandex.practicum.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.market.dto.OrderDto;
import ru.yandex.practicum.market.service.OrderService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/orders", "/buy"})
public class OrderViewController {

    private final OrderService orderService;

    @GetMapping
    public String getOrders(Model model) {
        List<OrderDto> orders= orderService.getOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{itemId}")
    public String getOrderById(@PathVariable Long itemId,
                               @RequestParam(required = false, defaultValue = "false") boolean newOrder,
                               Model model) {
        OrderDto order = orderService.getOrderById(itemId, newOrder);
        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping
    public String newOrder(Model model) {
        Long newOrderId = orderService.newOrder();
        return "redirect:/orders/" + newOrderId + "?newOrder=true";
    }

}


//GET /orders
//GET /orders/{id}?newOrder=[newOrder]
//POST /buy