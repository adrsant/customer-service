CREATE TABLE customer
(
    id   uuid                                  NOT NULL,
    name text CHECK (char_length(name) <= 100) NOT NULL,
    mail text CHECK (char_length(mail) <= 100) NOT NULL UNIQUE,
    CONSTRAINT customer_pk PRIMARY KEY (id)
);

CREATE TABLE favorite_product
(
    product_id  uuid NOT NULL,
    customer_id uuid NOT NULL,
    CONSTRAINT favorite_product_pk PRIMARY KEY (product_id, customer_id)
);

ALTER TABLE ONLY favorite_product
    ADD CONSTRAINT customer_favorite_product_fk
        FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE;

CREATE INDEX customer_favorite_idx ON favorite_product (customer_id);
