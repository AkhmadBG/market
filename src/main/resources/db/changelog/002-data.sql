INSERT INTO items (title, description, img_path, price)
VALUES ('test1', 'test1 description', '/images/cup_coffee.png', 100)
    ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price)
VALUES ('test2', 'test2 description', '/images/cup_coffee.png', 200)
    ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price)
VALUES ('test3', 'test3 description', '/images/cup_coffee.png', 300)
    ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price)
VALUES ('test4', 'test4 description', '/images/cup_coffee.png', 400)
    ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price)
VALUES ('check_test', 'check_test description', '/images/cup_coffee.png', 400)
    ON CONFLICT DO NOTHING;