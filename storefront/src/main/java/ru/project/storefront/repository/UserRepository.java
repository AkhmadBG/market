package ru.project.storefront.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByUsername(String username);

}