package ru.yandex.practicum.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.market.entity.ItemInCart;

import java.util.Optional;

@Repository
public interface ItemInCartRepository extends JpaRepository<ItemInCart, Long> {

    Optional<ItemInCart> findByItem_ItemId(Long itemId);

}
