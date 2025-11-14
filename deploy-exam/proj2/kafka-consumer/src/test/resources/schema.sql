DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS sales_items;
DROP TABLE IF EXISTS sales_summary;

CREATE TABLE sales
(
    id          VARCHAR(40) PRIMARY KEY, -- 売上情報ID
    date_time   TIMESTAMP NOT NULL,      -- 購入日時
    discount    INT       NOT NULL,      -- 割引
    paid_point  INT       NOT NULL,      -- ポイント支払い
    paid_cash   INT       NOT NULL,      -- 現金支払い
    total       INT       NOT NULL,      -- 合計金額
    given_point INT       NOT NULL       -- 付与ポイント
);

CREATE TABLE sales_items
(
    id         VARCHAR(40) PRIMARY KEY,
    sales_id   VARCHAR(40),                      -- 売上情報ID (外部キー)
    item_id    INT          NOT NULL,            -- 商品ID
    unit_price INT          NOT NULL,            -- 単価
    quantity   INT          NOT NULL,            -- 数量
    subtotal   INT          NOT NULL,            -- 小計
    FOREIGN KEY (sales_id) REFERENCES sales (id) -- 外部キー制約
);

CREATE TABLE sales_summary
(
    id             VARCHAR(40) PRIMARY KEY, -- 売上情報ID
    item_id        INT,                     -- 商品ID
    date           DATE NOT NULL,           -- 購入日時
    total_amount   INT  NOT NULL,           -- 売上金額
    total_quantity INT  NOT NULL            -- 売上点数
);
-- sales_summary テーブルに一意制約を追加
ALTER TABLE sales_summary
    ADD CONSTRAINT unique_item_date UNIQUE (item_id, date);

CREATE INDEX idx_sales_date_time ON sales (date_time);
CREATE INDEX idx_sales_items_sales_id ON sales_items (sales_id);
CREATE INDEX idx_sales_items_item_id ON sales_items (item_id);
CREATE INDEX idx_sales_summary_item_id ON sales_summary (item_id);
CREATE INDEX idx_sales_summary_date ON sales_summary (date);
CREATE INDEX idx_sales_summary_date_item_id ON sales_summary (date, item_id);
