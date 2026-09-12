CREATE INDEX idx_product_slug
ON products(slug);

CREATE INDEX idx_product_sku
ON products(sku);

CREATE INDEX idx_product_brand
ON products(brand_id);

CREATE INDEX idx_product_category
ON products(category_id);

CREATE INDEX idx_product_status
ON products(status);