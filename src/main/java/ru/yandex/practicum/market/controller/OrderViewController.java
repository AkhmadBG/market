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
public class OrderViewController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<OrderDto> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String getOrderById(@PathVariable(name = "id") Long orderId,
                               @RequestParam(required = false, defaultValue = "false") boolean newOrder,
                               Model model) {
        OrderDto order = orderService.getOrderById(orderId, newOrder);
        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping("/buy")
    public String newOrder() {
        Long newOrderId = orderService.newOrder();
        return "redirect:/orders/" + newOrderId + "?newOrder=true";
    }

}