package ru.project.storefront.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.project.storefront.service.MarketService;

@Controller
@RequiredArgsConstructor
public class OrderViewController {

    private final MarketService marketService;

    @GetMapping("/orders")
    public Mono<String> getOrders(Model model) {
        return marketService.getOrders()
                .collectList()
                .map(orders -> {
                    model.addAttribute("orders", orders);
                    return "orders";
                });

    }

    @GetMapping("/orders/{id}")
    public Mono<String> getOrderById(@PathVariable(name = "id") Long orderId,
                                     @RequestParam(required = false, defaultValue = "false") boolean newOrder,
                                     Model model) {
        return marketService.getOrderById(orderId)
                .map(orderDto -> {
                    model.addAttribute("order", orderDto);
                    model.addAttribute("newOrder", newOrder);
                    return "order";
                });
    }

    @PostMapping("/buy")
    public Mono<String> createOrder() {
        return marketService.createOrder()
                .map(orderDto -> "redirect:/orders/" + orderDto.orderId() + "?newOrder=true");
    }

}