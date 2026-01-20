CREATE TABLE orders (
    order_id   VARCHAR(36) PRIMARY KEY,
    user_id    VARCHAR(64) NOT NULL,
    state      VARCHAR(32) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    version    INT         NOT NULL
);

CREATE TABLE order_items (
    id         BIGSERIAL PRIMARY KEY,
    order_id  VARCHAR(36) NOT NULL,
    product_id VARCHAR(64) NOT NULL,
    quantity   INT NOT NULL,
    price      NUMERIC(12,2) NOT NULL,

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(order_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
