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
create sequence public.user_notification_seq start 1 increment 1;
create sequence public.product_review_seq start 1 increment 1;
create sequence public.discount_seq start 1 increment 1;

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
    description varchar(255),
    image varchar(255),
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
    id       bigint  not null,
    email    varchar(255),
    name     varchar(255),
    password varchar(255),
    role     varchar(255),
    primary key (id)
);
DROP TABLE IF EXISTS public.user_notification CASCADE;
create table public.user_notification (id int8 not null, notification int4, user_id int8, primary key (id));

drop table if exists public.product_review cascade;
create table public.product_review (id int8 not null, review varchar(1000), stars int4 not null, product_id int8, user_id int8, primary key (id));

drop table if exists public.discount cascade;
create table public.discount (id int8 not null, discount_price numeric(19, 2), product_id int8, primary key (id));

alter table if exists public.orders_details add constraint order_details unique (details_id);
alter table if exists public.buckets add constraint bucket_to_user foreign key (user_id) references public.users;
alter table if exists public.buckets_product add constraint bucket_product_to_product foreign key (product_id) references public.products;
alter table if exists public.buckets_product add constraint bucket_to_product foreign key (bucket_id) references public.buckets;
alter table if exists public.orders add constraint order_to_user foreign key (user_id) references public.users;
alter table if exists public.orders_details add constraint order_details_to_order foreign key (order_id) references public.orders;
alter table if exists public.orders_details add constraint order_details_to_product foreign key (product_id) references public.products;
alter table if exists public.orders_details add constraint order_details_id_to_details_id foreign key (details_id) references public.orders_details;
alter table if exists public.products_categories add constraint categories_to_categories_id foreign key (category_id) references public.categories;
alter table if exists public.products_categories add constraint categories_to_product_id foreign key (product_id) references public.products;
alter table if exists public.user_notification add constraint FKc2d7aih8weit50jlu4q57cvs foreign key (user_id) references public.users;
alter table if exists public.product_review add constraint FKlkf2n9dkjx32vcqqmds9v62 foreign key (product_id) references public.products;
alter table if exists public.product_review add constraint FKib6mkfaqaj0beph37y4qxmu9x foreign key (user_id) references public.users;
alter table if exists public.discount add constraint FKr4tq0e68q1e2id6odo428vawp foreign key (product_id) references public.products;