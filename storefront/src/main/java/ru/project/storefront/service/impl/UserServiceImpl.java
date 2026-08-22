package ru.project.storefront.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.User;
import ru.project.storefront.repository.UserRepository;
import ru.project.storefront.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context
                        .getAuthentication().getName())
                .flatMap(userRepository::findByUsername);
    }

}