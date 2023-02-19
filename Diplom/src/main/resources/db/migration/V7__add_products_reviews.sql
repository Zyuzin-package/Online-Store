INSERT INTO product_review (id, review, stars, product_id, user_id)
VALUES (1,'Good product, tasty!',5,11,1),
       (2,'Good product, tasty!',4,22,1),
       (3,'Norm product',3,33,1),
       (4,'Not good product, un tasty!',2,44,1),
       (5,'Bad product',1,55,1),
       (6,'Good product',5,66,1);
ALTER SEQUENCE product_review_seq RESTART with 7;