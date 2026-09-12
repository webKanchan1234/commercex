CREATE TABLE categories
(
    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL UNIQUE,

    slug VARCHAR(120) NOT NULL UNIQUE,

    description VARCHAR(500),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    version BIGINT,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);