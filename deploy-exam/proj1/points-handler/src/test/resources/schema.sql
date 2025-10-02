DROP TABLE IF EXISTS customer_points;

CREATE TABLE customer_points
(
    customer_id    VARCHAR(40) PRIMARY KEY,
    current_points INTEGER NOT NULL
);