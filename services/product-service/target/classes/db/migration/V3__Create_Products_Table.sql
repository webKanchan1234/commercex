CREATE TABLE products
(
    id UUID PRIMARY KEY,

    sku VARCHAR(50) NOT NULL UNIQUE,

    name VARCHAR(200) NOT NULL,

    slug VARCHAR(250) NOT NULL UNIQUE,

    description VARCHAR(5000),

    price NUMERIC(12,2) NOT NULL,

    discount_price NUMERIC(12,2),

    status VARCHAR(30) NOT NULL,

    stock_tracked BOOLEAN NOT NULL DEFAULT TRUE,

    brand_id UUID NOT NULL,

    category_id UUID NOT NULL,

    version BIGINT,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_product_brand
        FOREIGN KEY (brand_id)
        REFERENCES brands(id),

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
);