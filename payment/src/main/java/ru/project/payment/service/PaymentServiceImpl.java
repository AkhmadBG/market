package ru.project.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.project.payment.dto.BalanceResponse;
import ru.project.payment.dto.PaymentRequest;
import ru.project.payment.dto.PaymentResponse;
import ru.project.payment.repository.AccountRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountRepository accountRepository;

    @Override
    public Mono<BalanceResponse> getBalance(Long userId) {
        return accountRepository.findAccountByUserId(userId)
                .map(account -> new BalanceResponse(account.getBalance().doubleValue()));
    }

    @Override
    public Mono<PaymentResponse> pay(PaymentRequest request) {

        return accountRepository.findAccountByUserId(request.getUserId())
                .flatMap(account -> {

                    BigDecimal amount = BigDecimal.valueOf(request.getAmount());

                    if (account.getBalance().compareTo(amount) < 0) {
                        return Mono.just(new PaymentResponse(false, account.getBalance().doubleValue()));
                    }

                    account.setBalance(account.getBalance().subtract(amount));

                    return accountRepository.save(account)
                            .map(savedAccount -> new PaymentResponse(true, savedAccount.getBalance().doubleValue()));
                });
    }

}