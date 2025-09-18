DROP TABLE IF EXISTS customer_points;

CREATE TABLE customer_points
(
    customer_id VARCHAR(40) PRIMARY KEY,
    point      INTEGER NOT NULL
);

INSERT INTO customer_points (customer_id, point)
VALUES ('testId', 100);