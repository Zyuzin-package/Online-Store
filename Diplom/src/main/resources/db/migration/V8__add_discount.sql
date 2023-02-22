INSERT INTO discount (id, discount_price, product_id)
VALUES (2,350,11),
       (3,35,22),
       (4,55,33),
       (5,110,44),
       (6,50,55),
       (7,50,66),
       (8,0,78);
ALTER SEQUENCE discount_seq RESTART with 9;