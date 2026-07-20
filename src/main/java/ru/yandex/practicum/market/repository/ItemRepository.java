package ru.yandex.practicum.market.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.yandex.practicum.market.entity.Item;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {

    Flux<Item> findByTitleOrDescription(String title, String description, Pageable pageable);

}