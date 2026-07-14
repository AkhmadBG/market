package ru.yandex.practicum.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.market.entity.ItemInCart;
import ru.yandex.practicum.market.enums.CartStatus;

import java.util.Optional;

@Repository
public interface ItemInCartRepository extends JpaRepository<ItemInCart, Long> {

    Optional<ItemInCart> findByCart_CartStatusAndItem_ItemId(CartStatus cartStatus, Long itemId);

}