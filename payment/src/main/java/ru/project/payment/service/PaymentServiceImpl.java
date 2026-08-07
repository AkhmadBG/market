package ru.project.payment.service;

import org.springframework.stereotype.Service;
import ru.project.payment.dto.BalanceResponse;
import ru.project.payment.dto.PaymentRequest;
import ru.project.payment.dto.PaymentResponse;

@Service
public class PaymentServiceImpl implements PaymentService {

    private Double balance = 15000.0;

    @Override
    public BalanceResponse getBalance() {
        return new BalanceResponse(balance);
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        if (balance >= request.getAmount()) {
            balance -= request.getAmount();
            return new PaymentResponse(true, balance);
        }
        return new PaymentResponse(false, balance);
    }

}