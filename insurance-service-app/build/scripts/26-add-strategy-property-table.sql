set search_path to 'insurance_service';

--Таблица наборов данных стратегий
CREATE TABLE IF NOT EXISTS strategy_property (
 id             BIGSERIAL NOT NULL,
 version        BIGINT    NOT NULL,
 rate           NUMERIC,
 ticker         VARCHAR(20),
 expirationDate TIMESTAMP,
 nzbaDate       TIMESTAMP,
 strategy_id    BIGINT NOT NULL,
 PRIMARY KEY (id),
 CONSTRAINT strategy_property_fkey FOREIGN KEY (strategy_id) REFERENCES strategy (id)
);

--Перененос данных из существующих стратегий
INSERT INTO strategy_property (version, rate, ticker, strategy_id)
SELECT 0, COALESCE(s.rate, 0), s.ticker, s.id
FROM strategy s
WHERE NOT EXISTS (SELECT sp.id FROM strategy_property sp WHERE COALESCE(sp.rate, 0) = COALESCE(s.rate, 0) AND sp.strategy_id = s.id);

ALTER TABLE strategy_property ALTER COLUMN rate SET NOT NULL;

--Удаление старых полей
ALTER TABLE strategy DROP COLUMN IF EXISTS rate;
ALTER TABLE strategy DROP COLUMN IF EXISTS ticker;