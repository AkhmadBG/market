package ru.project.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.project.payment.api.PaymentApi;
import ru.project.payment.dto.BalanceResponse;
import ru.project.payment.dto.PaymentRequest;
import ru.project.payment.dto.PaymentResponse;
import ru.project.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    @Override
    public Mono<ResponseEntity<BalanceResponse>> getBalance(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(paymentService.getBalance()));
    }

    @Override
    public Mono<ResponseEntity<PaymentResponse>> pay(
            Mono<PaymentRequest> paymentRequest,
            ServerWebExchange exchange) {

        return paymentRequest
                .map(paymentService::pay)
                .map(ResponseEntity::ok);
    }

}