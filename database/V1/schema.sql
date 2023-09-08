drop schema ecommerce_shipments_db;
create schema ecommerce_shipments_db;
use ecommerce_shipments_db;


create table order_line_items
(
    id          bigint auto_increment
        primary key,
    order_id    bigint         not null,
    product_id  bigint         not null,
    shipment_id bigint         null,
    quantity    int            not null,
    price       decimal(38, 2) not null,
    total_price decimal(38, 2) null,
    created_dt  datetime(6)    null,
    updated_dt  datetime(6)    null
);

create table shipment_account
(
    created_dt    datetime(6)             null,
    id            bigint auto_increment
        primary key,
    updated_dt    datetime(6)             null,
    email_address varchar(200) default '' not null,
    first_name    varchar(200) default '' not null,
    last_name     varchar(200) default '' not null,
    constraint UK_jy42o6hnfujo1n6a081n4ir44
        unique (email_address)
);

create table shipment_address
(
    state       varchar(2)   default '' not null,
    created_dt  datetime(6)             null,
    id          bigint auto_increment
        primary key,
    updated_dt  datetime(6)             null,
    postal_code varchar(10)  default '' not null,
    address1    varchar(200) default '' not null,
    address2    varchar(200) default '' null,
    city        varchar(200) default '' not null,
    country     varchar(100) default '' not null,
    province    varchar(200) default '' null
);

create table shipment_account_addresses
(
    addresses_id        bigint not null,
    shipment_account_id bigint not null,
    primary key (addresses_id, shipment_account_id),
    constraint UK_a0qr179iltgiuj4s94wshmb4a
        unique (addresses_id),
    constraint FK5tga1uurk98n9ew70ta9pfsrd
        foreign key (shipment_account_id) references shipment_account (id),
    constraint FKla49olrmp890do8p58lcxe8is
        foreign key (addresses_id) references shipment_address (id)
);

create table shipments
(
    created_dt              datetime(6) null,
    id                      bigint auto_increment
        primary key,
    shipment_account_id     bigint      not null,
    shipment_address_id     bigint      not null,
    updated_dt              datetime(6) null,
    value_for_delivery_date datetime(6) null,
    value_for_shipped_date  datetime(6) null,
    constraint UK_f20gjljlau5b7bf2080bnm43x
        unique (shipment_account_id),
    constraint UK_ovnq79on77dn867rbqxryjktw
        unique (shipment_address_id),
    constraint FKe03rdv5hku9m4adqc3w161pn
        foreign key (shipment_account_id) references shipment_account (id),
    constraint FKhbeqi97sdt2u5tcji83csmnhg
        foreign key (shipment_address_id) references shipment_address (id)
);

create table shipments_order_line_items
(
    order_line_items_id bigint not null,
    shipments_id        bigint not null,
    primary key (order_line_items_id, shipments_id),
    constraint UK_2rc6coe346dx1h8o2by305x2t
        unique (order_line_items_id),
    constraint FK33e65i1af0wmtri969hcj3rn2
        foreign key (shipments_id) references shipments (id),
    constraint FK3jumhfv72blqiur9vxka2fud3
        foreign key (order_line_items_id) references order_line_items (id)
);

create table shipping_account
(
    id            bigint auto_increment
        primary key,
    first_name    varchar(200) default '' not null,
    last_name     varchar(200) default '' not null,
    email_address varchar(200) default '' not null,
    created_dt    datetime                null,
    updated_dt    datetime                null,
    constraint uc_shipping_account_emailaddress
        unique (email_address)
);

create table shipping_address
(
    id             bigint auto_increment
        primary key,
    street_address varchar(200) default '' not null,
    second_address varchar(200) default '' null,
    city           varchar(200) default '' not null,
    state          varchar(2)   default '' not null,
    province       varchar(200) default '' null,
    postal_code    varchar(10)  default '' not null,
    country        varchar(100) default '' not null
);

create table shipping_account_addresses
(
    shipping_account_id bigint not null,
    addresses_id        bigint not null,
    primary key (shipping_account_id, addresses_id),
    constraint uc_shipping_account_addresses_addresses
        unique (addresses_id),
    constraint fk_shiaccadd_on_shipping_account
        foreign key (shipping_account_id) references shipping_account (id),
    constraint fk_shiaccadd_on_shipping_address
        foreign key (addresses_id) references shipping_address (id)
);


ALTER TABLE shipments
    ADD CONSTRAINT UC_Shipment UNIQUE (ID);