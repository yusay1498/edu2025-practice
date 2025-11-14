DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS item;

CREATE TABLE customer(
                         "id" integer PRIMARY KEY,
                         "name" varchar NOT NULL,
                         "gender" varchar NOT NULL
);

CREATE TABLE item(
                     "id" integer PRIMARY KEY,
                     "name" varchar NOT NULL,
                     "price" integer,
                     "sell_price" integer
);