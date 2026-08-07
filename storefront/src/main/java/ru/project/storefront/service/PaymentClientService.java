package ru.project.storefront.service;

import reactor.core.publisher.Mono;
import ru.project.storefront.dto.BalanceResponse;
import ru.project.storefront.dto.PaymentResponse;

public interface PaymentClientService {

    Mono<BalanceResponse> getBalance();

    Mono<PaymentResponse> pay(Double amount);

}