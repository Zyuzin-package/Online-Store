drop table if exists public.buckets_product cascade;
drop table if exists public.categories cascade;
drop table if exists public.discount cascade;
drop table if exists public.orders cascade;
drop table if exists public.orders_details cascade;
drop table if exists public.product_review cascade;
drop table if exists public.products cascade;
drop table if exists public.products_categories cascade;
drop table if exists public.users cascade;
drop table if exists public.visit_stats cascade;
drop table if exists public.frequency_add_to_cart_stats cascade;

drop sequence if exists public.bucket_seq;
drop sequence if exists public.category_seq;
drop sequence if exists public.discount_seq;
drop sequence if exists public.order_details_seq;
drop sequence if exists public.order_seq;
drop sequence if exists public.product_review_seq;
drop sequence if exists public.product_seq;
drop sequence if exists public.user_notification_seq;
drop sequence if exists public.user_seq;
drop sequence if exists public.visit_stats_seq;
drop sequence if exists public.frequency_add_to_cart_stats_seq;

alter table if exists public.discount
    drop constraint if exists FKr4tq0e68q1e2id6odo428vawp;
alter table if exists public.orders
    drop constraint if exists FK32ql8ubntj5uh44ph9659tiih;
alter table if exists public.orders_details
    drop constraint if exists FKs0r9x49croribb4j6tah648gt;
alter table if exists public.orders_details
    drop constraint if exists FKqrd3t5p4n207uchru3wnbxo9g;
alter table if exists public.product_review
    drop constraint if exists FKlkf2n9dkjx32vcqqmds9v62;
alter table if exists public.product_review
    drop constraint if exists FKib6mkfaqaj0beph37y4qxmu9x;
alter table if exists public.products_categories
    drop constraint if exists FKqt6m2o5dly3luqcm00f5t4h2p;
alter table if exists public.products_categories
    drop constraint if exists FKtj1vdea8qwerbjqie4xldl1el;

create table public.buckets
(
    id      int8 not null,
    user_id int8,
    primary key (id)
);
create table public.user_notification
(
    id       int8 not null,
    message  varchar(255),
    url      varchar(255),
    url_text varchar(255),
    user_id  int8,
    primary key (id)
);
create sequence public.bucket_seq start 1 increment 1;
create sequence public.category_seq start 1 increment 1;
create sequence public.discount_seq start 1 increment 1;
create sequence public.order_details_seq start 1 increment 1;
create sequence public.order_seq start 1 increment 1;
create sequence public.product_review_seq start 1 increment 1;
create sequence public.product_seq start 1 increment 1;
create sequence public.user_notification_seq start 1 increment 1;
create sequence public.user_seq start 1 increment 1;
create sequence public.visit_stats_seq start 1 increment 1;
create sequence public.frequency_add_to_cart_stats_seq start 1 increment 1;

create table public.buckets_product
(
    bucket_id  int8 not null,
    product_id int8 not null
);
create table public.categories
(
    id    int8 not null,
    title varchar(255),
    primary key (id)
);
create table public.discount
(
    id             int8   not null,
    discount_price float8 not null,
    product_id     int8,
    primary key (id)
);
create table public.orders
(
    id      int8   not null,
    address varchar(255),
    created timestamp,
    status  varchar(255),
    sum     float8 not null,
    updated timestamp,
    user_id int8,
    primary key (id)
);
create table public.orders_details
(
    id               int8   not null,
    amount           int4   not null,
    price            float8 not null,
    product_id       int8,
    order_details_id int8,
    primary key (id)
);
create table public.product_review
(
    id         int8 not null,
    review     varchar(1000),
    stars      int4 not null,
    product_id int8,
    user_id    int8,
    primary key (id)
);
create table public.products
(
    id          int8   not null,
    description varchar(255),
    image       varchar(255),
    price       float8 not null,
    title       varchar(255),
    primary key (id)
);
create table public.products_categories
(
    product_id  int8 not null,
    category_id int8 not null
);
create table public.users
(
    id              int8 not null,
    activation_code varchar(255),
    email           varchar(255),
    name            varchar(255),
    password        varchar(255),
    role            varchar(255),
    primary key (id)
);
create table public.visit_stats
(
    id         int8 not null,
    created    timestamp,
    product_id int8,
    primary key (id)
);
create table public.frequency_add_to_cart_stats
(
    id         int8 not null,
    created    timestamp,
    product_id int8,
    primary key (id)
);
alter table if exists public.buckets
    add constraint FKnl0ltaj67xhydcrfbq8401nvj foreign key (user_id) references public.users;
alter table if exists public.user_notification
    add constraint FKc2d7aih8weit50jlu4q57cvs foreign key (user_id) references public.users;
alter table if exists public.buckets_product
    add constraint FKic6ur6tnwvwrxpomniehbv383 foreign key (product_id) references public.products;
alter table if exists public.buckets_product
    add constraint FKje1rq0ugm3ggvsstdi47ep6we foreign key (bucket_id) references public.buckets;
alter table if exists public.discount
    add constraint FKr4tq0e68q1e2id6odo428vawp foreign key (product_id) references public.products;
alter table if exists public.orders
    add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references public.users;
alter table if exists public.orders_details
    add constraint FKs0r9x49croribb4j6tah648gt foreign key (product_id) references public.products;
alter table if exists public.orders_details
    add constraint FKqrd3t5p4n207uchru3wnbxo9g foreign key (order_details_id) references public.orders;
alter table if exists public.product_review
    add constraint FKlkf2n9dkjx32vcqqmds9v62 foreign key (product_id) references public.products;
alter table if exists public.product_review
    add constraint FKib6mkfaqaj0beph37y4qxmu9x foreign key (user_id) references public.users;
alter table if exists public.products_categories
    add constraint FKqt6m2o5dly3luqcm00f5t4h2p foreign key (category_id) references public.categories;
alter table if exists public.products_categories
    add constraint FKtj1vdea8qwerbjqie4xldl1el foreign key (product_id) references public.products;
