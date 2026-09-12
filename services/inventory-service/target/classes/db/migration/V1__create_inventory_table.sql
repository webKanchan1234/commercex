CREATE TABLE inventory
(
    id                  UUID PRIMARY KEY,

    product_id          UUID NOT NULL UNIQUE,

    quantity            INTEGER NOT NULL,

    reserved_quantity   INTEGER NOT NULL DEFAULT 0,

    warehouse           VARCHAR(100) NOT NULL,

    status              VARCHAR(30) NOT NULL,

    version             BIGINT NOT NULL DEFAULT 0,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);