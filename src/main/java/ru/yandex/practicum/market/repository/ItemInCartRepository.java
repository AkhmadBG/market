package ru.yandex.practicum.market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.market.entity.Item;
import ru.yandex.practicum.market.entity.ItemInCart;

import java.util.Optional;

@Repository
public interface ItemInCartRepository extends JpaRepository<ItemInCart, Long> {

    Optional<ItemInCart> findByItem_ItemId(Long itemId);

    Page<ItemInCart> findByItem_TitleContainingIgnoreCaseOrItemDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);


}