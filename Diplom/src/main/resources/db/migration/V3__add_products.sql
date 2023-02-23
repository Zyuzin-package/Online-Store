INSERT INTO products (id, price, title, image, description)
VALUES (11, 450.0, 'Meat1', '/img/1.jpg', 'description'),
       (22, 45.0, 'Meat2', '/img/2.jpg', 'description'),
       (33, 65.0, 'Cheese1', '/img/3.jpg', 'description'),
       (44, 115.0, 'Cheese2', '/img/4.jpg', 'description'),
       (55, 58.0, 'Spice1', '/img/5.jpg', 'description'),
       (66, 58.0, 'Spice2', '/img/6.jpg', 'description'),
       (78, 78, 'Gofman', '/img/gof78man.jpg',
        'Мелкий предприниматель еврейской национальности из города Херсон, ставший известным благодаря своим видеороликам, в которых рассказывает о «своей биографии в контексте истории развития Земли и мироздания в целом, их взаимопереплетении и взаимовлиянии».');
ALTER SEQUENCE product_seq RESTART WITH 90;
INSERT INTO categories (id, title)
VALUES (1, 'meat'),
       (2, 'cheese'),
       (3, 'spice');
ALTER SEQUENCE category_seq RESTART WITH 4;