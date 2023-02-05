
INSERT INTO products (id, price, title)
VALUES (1, 450.0, 'Meat1'),
       (2, 45.0, 'Meat2'),
       (3, 65.0, 'Cheese1'),
       (4, 115.0, 'Cheese2'),
       (5, 58.0, 'Spice1'),
       (6, 58.0, 'Spice2');

INSERT INTO categories (id, title)
VALUES (1, 'meat'),
       (2, 'cheese'),
       (3, 'spice');
-- INSERT INTO  products (id, price, title)
-- VALUES (1,450.0,'Cheese'),
--        (2, 45.0,'Beer'),
--        (3,65.0,'Milk'),
--        (4,115.0,'Tomato'),
--        (5,58.0,'Bread');
--
-- ALTER SEQUENCE product_seq RESTART WITH 6;