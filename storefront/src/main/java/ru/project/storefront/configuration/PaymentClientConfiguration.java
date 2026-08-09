package ru.project.storefront.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.project.storefront.ApiClient;
import ru.project.storefront.client.PaymentApi;

@Configuration
public class PaymentClientConfiguration {

    @Bean
    public ApiClient apiClient() {
        return new ApiClient().setBasePath("http://localhost:8081");
    }

    @Bean
    public PaymentApi paymentApi(ApiClient apiClient) {
        return new PaymentApi(apiClient);
    }

}