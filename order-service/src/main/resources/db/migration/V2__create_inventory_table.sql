CREATE TABLE orders.inventory (
    product_id    VARCHAR(64) PRIMARY KEY,
    available_qty INT NOT NULL,
    version       INT NOT NULL
);

CREATE INDEX idx_inventory_product_id ON orders.inventory(product_id);
