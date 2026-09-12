CREATE TABLE order_items
(
    id              BINARY(16)      NOT NULL,

    order_id        BINARY(16)      NOT NULL,

    product_id      BINARY(16)      NOT NULL,

    product_name    VARCHAR(255)    NOT NULL,

    price           DECIMAL(19,2)   NOT NULL,

    quantity        INT             NOT NULL,

    subtotal        DECIMAL(19,2)   NOT NULL,

    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
                                      ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
            REFERENCES orders(id)
            ON DELETE CASCADE
);