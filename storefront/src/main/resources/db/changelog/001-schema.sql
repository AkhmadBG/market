CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT uq_users_username UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS items (
    item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    img_path VARCHAR(255),
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    CONSTRAINT uq_items_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS carts (
    cart_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total NUMERIC(10, 2) NOT NULL,
    cart_status VARCHAR(25) NOT NULL,
    CONSTRAINT fk_carts_users FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_sum NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_orders_users FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS items_in_carts (
    item_in_cart_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id BIGINT NOT NULL,
    cart_id BIGINT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    count INTEGER NOT NULL CHECK (count >= 0),
    CONSTRAINT fk_items_in_carts_items FOREIGN KEY (item_id) REFERENCES items (item_id),
    CONSTRAINT fk_items_in_carts_carts FOREIGN KEY (cart_id) REFERENCES carts (cart_id) ON DELETE CASCADE,
    CONSTRAINT uq_cart_item UNIQUE (cart_id, item_id)
);

CREATE TABLE IF NOT EXISTS items_in_orders (
    item_in_order_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    count INTEGER NOT NULL CHECK (count >= 0),
    CONSTRAINT fk_items_in_orders_items FOREIGN KEY (item_id) REFERENCES items (item_id),
    CONSTRAINT fk_items_in_orders_orders FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT uq_order_item UNIQUE (order_id, item_id)
);