INSERT INTO product_review (id, review, stars, product_id, user_id)
VALUES (1,'Вкусное мясо, очень понравилось!',5,11,3),
       (2,'Некачественный продукт',2,11,1);
ALTER SEQUENCE product_review_seq RESTART with 11;