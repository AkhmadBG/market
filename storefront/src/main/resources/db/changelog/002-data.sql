INSERT INTO items (title, description, img_path, price)
VALUES ('test1', 'test1 description', '/images/cup_coffee.png', 1000) ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price)
VALUES ('test2', 'test2 description', '/images/cup_coffee.png', 2000) ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price)
VALUES ('test3', 'test3 description', '/images/cup_coffee.png', 3000) ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price)
VALUES ('test4', 'test4 description', '/images/cup_coffee.png', 4000) ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price)
VALUES ('check_test', 'check_test description', '/images/cup_coffee.png', 5000) ON CONFLICT DO NOTHING;

INSERT INTO users (username, password)
VALUES ('test1', '$2y$10$1MYyy9cVnY2TOpFU1q8pUuHVAB8.DjFYMf22aSEJxQy9e9nLFmEWW') ON CONFLICT DO NOTHING;

INSERT INTO users (username, password)
VALUES ('test2', '$2y$10$1MYyy9cVnY2TOpFU1q8pUuHVAB8.DjFYMf22aSEJxQy9e9nLFmEWW') ON CONFLICT DO NOTHING;

INSERT INTO users (username, password)
VALUES ('test3', '$2y$10$1MYyy9cVnY2TOpFU1q8pUuHVAB8.DjFYMf22aSEJxQy9e9nLFmEWW') ON CONFLICT DO NOTHING;

INSERT INTO users (username, password)
VALUES ('test4', '$2y$10$1MYyy9cVnY2TOpFU1q8pUuHVAB8.DjFYMf22aSEJxQy9e9nLFmEWW') ON CONFLICT DO NOTHING;