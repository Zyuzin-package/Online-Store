DROP TABLE IF EXISTS public.buckets CASCADE;
create table public.buckets
(
    id      bigint not null,
    user_id bigint,
    primary key (id)
);
create sequence public.bucket_seq start with 1 increment by 1;
create sequence public.category_seq start with 1 increment by 1;
create sequence public.order_details_seq start with 1 increment by 1;
create sequence public.order_seq start with 1 increment by 1;
create sequence public.product_seq start with 1 increment by 1;
create sequence public.user_seq start with 1 increment by 1;

DROP TABLE IF EXISTS public.buckets_product CASCADE;
create table public.buckets_product
(
    bucket_id  bigint not null,
    product_id bigint not null
);

DROP TABLE IF EXISTS public.categories CASCADE;
create table public.categories
(
    id    bigint not null,
    title varchar(255),
    primary key (id)
);

DROP TABLE IF EXISTS public.orders CASCADE;
create table public.orders
(
    id      bigint not null,
    address varchar(255),
    created timestamp(6),
    updated timestamp(6),
    status  varchar(255),
    sum     numeric(38, 2),
    user_id bigint,
    primary key (id)
);

DROP TABLE IF EXISTS public.orders_details CASCADE;
create table public.orders_details
(
    id         bigint not null,
    amount     numeric(38, 2),
    price      numeric(38, 2),
    order_id   bigint,
    product_id bigint,
    details_id bigint not null,
    primary key (id)
);

DROP TABLE IF EXISTS public.products CASCADE;
create table public.products
(
    id    bigint not null,
    price numeric(38, 2),
    title varchar(255),
    primary key (id)
);

DROP TABLE IF EXISTS public.products_categories CASCADE;
create table public.products_categories
(
    product_id  bigint not null,
    category_id bigint not null
);

DROP TABLE IF EXISTS public.users CASCADE;
create table public.users
(
    id        bigint  not null,
    archive   boolean not null,
    email     varchar(255),
    name      varchar(255),
    password  varchar(255),
    role      varchar(255),
    bucket_id bigint,
    primary key (id)
);
alter table if exists public.orders_details add constraint UK_kk6y3pyhjt6kajomtjbhsoajo unique (details_id);
alter table if exists public.buckets add constraint FKnl0ltaj67xhydcrfbq8401nvj foreign key (user_id) references public.users;
alter table if exists public.buckets_product add constraint FKic6ur6tnwvwrxpomniehbv383 foreign key (product_id) references public.products;
alter table if exists public.buckets_product add constraint FKje1rq0ugm3ggvsstdi47ep6we foreign key (bucket_id) references public.buckets;
alter table if exists public.orders add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references public.users;
alter table if exists public.orders_details add constraint FK5o977kj2vptwo70fu7w7so9fe foreign key (order_id) references public.orders;
alter table if exists public.orders_details add constraint FKs0r9x49croribb4j6tah648gt foreign key (product_id) references public.products;
alter table if exists public.orders_details add constraint FKgvp1k7a3ubdboj3yhnawd5m1p foreign key (details_id) references public.orders_details;
alter table if exists public.products_categories add constraint FKqt6m2o5dly3luqcm00f5t4h2p foreign key (category_id) references public.categories;
alter table if exists public.products_categories add constraint FKtj1vdea8qwerbjqie4xldl1el foreign key (product_id) references public.products;
alter table if exists public.users add constraint FK8l2qc4c6gihjdyoch727guci foreign key (bucket_id) references public.buckets;

