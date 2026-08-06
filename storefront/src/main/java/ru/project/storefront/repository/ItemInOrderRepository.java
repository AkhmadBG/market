package ru.project.storefront.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.project.storefront.entity.ItemInOrder;

@Repository
public interface ItemInOrderRepository extends ReactiveCrudRepository<ItemInOrder, Long> {

    Flux<ItemInOrder> findAllByOrderId(Long orderId);

}