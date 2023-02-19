INSERT INTO discount (id, discount_price, product_id)
VALUES (1,350,11),
       (2,35,22),
       (3,55,33),
       (4,110,44),
       (5,50,55),
       (6,50,66);
ALTER SEQUENCE discount_seq RESTART with 7;