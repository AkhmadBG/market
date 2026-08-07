package ru.project.payment.service;

import ru.project.payment.dto.BalanceResponse;
import ru.project.payment.dto.PaymentRequest;
import ru.project.payment.dto.PaymentResponse;

public interface PaymentService {

    BalanceResponse getBalance();

    PaymentResponse pay(PaymentRequest request);

}