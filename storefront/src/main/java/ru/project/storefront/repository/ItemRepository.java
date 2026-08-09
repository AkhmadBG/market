package ru.project.storefront.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.Item;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {

    @Query("""
            SELECT *
            FROM items
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))
            ORDER BY price
            LIMIT :limit
            OFFSET :offset
            """)
    Flux<Item> searchAndOrderByPrice(String search, int limit, long offset);

    @Query("""
            SELECT *
            FROM items
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))
            ORDER BY title
            LIMIT :limit
            OFFSET :offset
            """)
    Flux<Item> searchAndOrderByTitle(String search, int limit, long offset);

    @Query("""
            SELECT *
            FROM items
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))
            LIMIT :limit
            OFFSET :offset
            """)
    Flux<Item> searchAndWithoutOrder(String search, int limit, long offset);

    @Query("""
            SELECT COUNT(*)
            FROM items
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Mono<Long> countSearch(String search);

}