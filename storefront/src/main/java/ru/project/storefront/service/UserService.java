package ru.project.storefront.service;

import reactor.core.publisher.Mono;
import ru.project.storefront.entity.User;

public interface UserService {

    Mono<User> getCurrentUser();

}