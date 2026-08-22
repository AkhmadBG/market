package ru.project.storefront.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.project.storefront.entity.ItemInCart;
import ru.project.storefront.enums.CartStatus;
import ru.project.storefront.repository.ItemInCartRepository;
import ru.project.storefront.service.ItemInCartService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ItemInCartServiceImpl implements ItemInCartService {

    private final ItemInCartRepository itemInCartRepository;
    private final DatabaseClient databaseClient;

    @Override
    public Mono<ItemInCart> findByUserIdAndCartStatusAndItemId(Long userId, CartStatus cartStatus, Long itemId) {
        return databaseClient.sql("""
                        SELECT
                            ic.item_in_cart_id,
                            ic.item_id,
                            ic.cart_id,
                            ic.price,
                            ic.count
                        FROM items_in_carts ic
                        JOIN carts c ON c.cart_id = ic.cart_id
                        JOIN users u ON u.user_id = c.user_id
                        WHERE u.user_id = :userId
                            AND c.cart_status = :cartStatus
                            AND ic.item_id = :itemId
                        """)
                .bind("userId", userId)
                .bind("cartStatus", cartStatus.name())
                .bind("itemId", itemId)
                .map((row, metadata) ->
                        new ItemInCart(
                                row.get("item_in_cart_id", Long.class),
                                row.get("item_id", Long.class),
                                row.get("cart_id", Long.class),
                                row.get("price", BigDecimal.class),
                                row.get("count", Integer.class)
                        )
                )
                .one();
    }

    @Override
    public Flux<ItemInCart> findAllByCartId(Long cartId) {
        return itemInCartRepository.findAllByCartId(cartId);
    }

    @Override
    public Mono<ItemInCart> save(ItemInCart itemInCart) {
        return itemInCartRepository.save(itemInCart);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return itemInCartRepository.deleteById(id);
    }

    @Override
    public Mono<ItemInCart> findByCartIdAndItemId(Long cartId, Long itemId) {
        return itemInCartRepository.findByCartIdAndItemId(cartId, itemId);
    }

}