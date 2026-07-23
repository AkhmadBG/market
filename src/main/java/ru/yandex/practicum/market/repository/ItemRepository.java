package ru.yandex.practicum.market.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.market.dto.SearchResult;
import ru.yandex.practicum.market.entity.Item;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {

    @Query("""
            SELECT *
            FROM items
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))
            ORDER BY title
            LIMIT :limit
            OFFSET :offset
            """)
    Flux<Item> search(String search, int limit, long offset);


    @Query("""
            SELECT COUNT(*)
            FROM items
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Mono<Long> countSearch(String search);
}

}