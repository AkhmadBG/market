package ru.yandex.practicum.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.market.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}