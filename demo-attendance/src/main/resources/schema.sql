DROP TABLE IF EXISTS attendances;

CREATE TABLE attendances
(
    id          VARCHAR(40) PRIMARY KEY,
    employee_id VARCHAR(40) NOT NULL,
    begin_work  TIMESTAMP   NOT NULL,
    finish_work TIMESTAMP
);

INSERT INTO attendances (id, employee_id, begin_work, finish_work)
VALUES ('abc123', 'ij92400038', '2025-09-01 09:30:00', '2025-09-01 18:00:00');