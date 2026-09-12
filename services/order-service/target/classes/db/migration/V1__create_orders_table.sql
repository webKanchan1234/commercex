CREATE TABLE orders
(
    id              BINARY(16)      NOT NULL,
    order_number    VARCHAR(50)     NOT NULL,
    customer_id     BINARY(16)      NOT NULL,

    status          VARCHAR(30)     NOT NULL,

    total_amount    DECIMAL(19,2)   NOT NULL,

    currency        VARCHAR(10)     NOT NULL,

    version         BIGINT          NOT NULL DEFAULT 0,

    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
                                      ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_orders_order_number
        UNIQUE (order_number)
);