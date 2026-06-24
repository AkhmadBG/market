CREATE TABLE IF NOT EXISTS items (
    item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    img_path VARCHAR(255),
    price NUMERIC(10, 2) NOT NULL CHECK (price > 0)
);

CREATE TABLE IF NOT EXISTS carts (
    cart_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    total NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT GENERATED ALWAYS AS Identity PRIMARY KEY,
    total_sum NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS items_in_carts (
    item_in_cart_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id INTEGER NOT NULL,
    cart_id INTEGER NOT NULL,
    count INTEGER NOT NULL CHECK (count > 0),
    CONSTRAINT fk_items_in_carts_items FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE,
    CONSTRAINT fk_items_in_carts_carts FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items_in_orders (
    item_in_order_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id INTEGER NOT NULL,
    order_id INTEGER NOT NULL,
    count INTEGER NOT NULL CHECK (count > 0),
    CONSTRAINT fk_items_in_orders_items FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE,
    CONSTRAINT fk_items_in_orders_orders FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);