-- テーブルの作成
CREATE TABLE IF NOT EXISTS attendances
(
    id          VARCHAR(40) PRIMARY KEY,
    employee_id VARCHAR(40) NOT NULL,
    begin_work  TIMESTAMP   NOT NULL,
    finish_work TIMESTAMP
);