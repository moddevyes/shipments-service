drop schema ecommerce_shipments_db;
create schema ecommerce_shipments_db;
use ecommerce_shipments_db;

-- shipment address
CREATE TABLE shipment_address
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    address1    VARCHAR(200)          NOT NULL,
    address2    VARCHAR(200)               DEFAULT '' NULL,
    city        VARCHAR(200)          NOT NULL,
    state       VARCHAR(2)            NOT NULL,
    province    VARCHAR(200)          null DEFAULT '' NULL,
    postal_code VARCHAR(10)           NOT NULL,
    country     VARCHAR(100)          NOT NULL,
    created_dt  datetime              NULL,
    updated_dt  datetime              NULL,
    CONSTRAINT pk_shipment_address PRIMARY KEY (id)
);

-- shipment account
CREATE TABLE shipment_account
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    first_name    VARCHAR(200)          NOT NULL,
    last_name     VARCHAR(200)          NOT NULL,
    email_address VARCHAR(200)          NOT NULL,
    created_dt    datetime              NULL,
    updated_dt    datetime              NULL,
    CONSTRAINT pk_shipment_account PRIMARY KEY (id)
);

CREATE TABLE shipment_account_addresses
(
    shipment_account_id BIGINT NOT NULL,
    addresses_id        BIGINT NOT NULL,
    CONSTRAINT pk_shipment_account_addresses PRIMARY KEY (shipment_account_id, addresses_id)
);

ALTER TABLE shipment_account_addresses
    ADD CONSTRAINT uc_shipment_account_addresses_addresses UNIQUE (addresses_id);

ALTER TABLE shipment_account_addresses
    ADD CONSTRAINT fk_shiaccadd_on_shipment_account FOREIGN KEY (shipment_account_id) REFERENCES shipment_account (id);

ALTER TABLE shipment_account_addresses
    ADD CONSTRAINT fk_shiaccadd_on_shipment_address FOREIGN KEY (addresses_id) REFERENCES shipment_address (id);

-- products, deliverydate, shippeddate / nada, dto only

-- order line items

CREATE TABLE order_line_items
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    order_id    BIGINT                NOT NULL,
    product_id  BIGINT                NOT NULL,
    shipment_id BIGINT                NULL,
    quantity    INT                   NOT NULL,
    price       DECIMAL               NOT NULL,
    total_price DECIMAL               NULL,
    created_dt  datetime              NULL,
    updated_dt  datetime              NULL,
    CONSTRAINT pk_order_line_items PRIMARY KEY (id)
);

-- shipments
CREATE TABLE shipments
(
    id                      BIGINT AUTO_INCREMENT NOT NULL,
    shipment_address_id     BIGINT                NOT NULL,
    shipment_account_id     BIGINT                NOT NULL,
    created_dt              datetime              NULL,
    updated_dt              datetime              NULL,
    value_for_shipped_date  datetime              NULL,
    value_for_delivery_date datetime              NULL,
    CONSTRAINT pk_shipments PRIMARY KEY (id)
);

CREATE TABLE shipments_order_line_items
(
    shipments_id        BIGINT NOT NULL,
    order_line_items_id BIGINT NOT NULL,
    CONSTRAINT pk_shipments_orderlineitems PRIMARY KEY (shipments_id, order_line_items_id)
);

ALTER TABLE shipments_order_line_items
    ADD CONSTRAINT uc_shipments_order_line_items_shidorid UNIQUE (shipments_id, order_line_items_id);

ALTER TABLE shipments
    ADD CONSTRAINT FK_SHIPMENTS_ON_SHIPMENTACCOUNT FOREIGN KEY (shipment_account_id) REFERENCES shipment_account (id);

ALTER TABLE shipments
    ADD CONSTRAINT FK_SHIPMENTS_ON_SHIPMENTADDRESS FOREIGN KEY (shipment_address_id) REFERENCES shipment_address (id);

ALTER TABLE shipments_order_line_items
    ADD CONSTRAINT fk_shiordlinite_on_order_line_items FOREIGN KEY (order_line_items_id) REFERENCES order_line_items (id);

ALTER TABLE shipments_order_line_items
    ADD CONSTRAINT fk_shiordlinite_on_shipments FOREIGN KEY (shipments_id) REFERENCES shipments (id);
