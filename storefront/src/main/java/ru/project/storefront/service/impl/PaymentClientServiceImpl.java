package ru.project.storefront.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.project.storefront.client.PaymentApi;
import ru.project.storefront.dto.BalanceResponse;
import ru.project.storefront.dto.PaymentRequest;
import ru.project.storefront.dto.PaymentResponse;
import ru.project.storefront.service.PaymentClientService;

@Service
@RequiredArgsConstructor
public class PaymentClientServiceImpl implements PaymentClientService {

    private final PaymentApi paymentApi;

    @Override
    public Mono<BalanceResponse> getBalance(Long userId) {
        return paymentApi.getBalance(userId);
    }

    @Override
    public Mono<PaymentResponse> pay(Long userId, Double amount) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setUserId(userId);
        return paymentApi.pay(paymentRequest);
    }

}