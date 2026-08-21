package ru.project.payment.service;

import reactor.core.publisher.Mono;
import ru.project.payment.dto.BalanceResponse;
import ru.project.payment.dto.PaymentRequest;
import ru.project.payment.dto.PaymentResponse;

public interface PaymentService {

    Mono<BalanceResponse> getBalance(Long userId);

    Mono<PaymentResponse> pay(PaymentRequest request);

}