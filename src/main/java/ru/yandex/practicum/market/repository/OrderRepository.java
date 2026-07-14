package ru.yandex.practicum.market.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.market.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {
            "itemsInOrder",
            "itemsInOrder.item"
    })
    List<Order> findAll();

    @EntityGraph(attributePaths = {
            "itemsInOrder",
            "itemsInOrder.item"
    })
    Optional<Order> findById(Long orderId);

}