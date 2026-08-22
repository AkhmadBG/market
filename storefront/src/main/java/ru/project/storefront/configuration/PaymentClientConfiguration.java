package ru.project.storefront.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import ru.project.storefront.ApiClient;
import ru.project.storefront.client.PaymentApi;

@Configuration
public class PaymentClientConfiguration {

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository) {

        var authorizedClientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);

        var authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
        );

        var authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public ApiClient apiClient(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {

        var oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        oauth2.setDefaultClientRegistrationId("payment");

        WebClient webClient = WebClient.builder()
                .filter(oauth2)
                .build();

        return new ApiClient(webClient)
                .setBasePath(paymentServiceUrl);
    }

    @Bean
    public PaymentApi paymentApi(ApiClient apiClient) {
        return new PaymentApi(apiClient);
    }

}