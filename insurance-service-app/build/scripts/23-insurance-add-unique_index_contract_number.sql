set search_path to 'insurance_service';

--Создание уникального индекса на поле номер договора в таблице договоров небыстрого старта
CREATE UNIQUE INDEX IF NOT EXISTS insurance_unique_contract_number
ON insurance (contract_number) WHERE contract_number IS NOT NULL;

--Создание уникального индекса на поля кодировка полиса и вариант - из просьбы Илоны в скайпе 19.12.18
--Илона 10:17Предлагаю сделать уникальный ключ по полям: кодировка программы и вариант программы страхования по таблице программ страхования. Это обеспечит уникальность номера договора в разрезе видов программы
CREATE UNIQUE INDEX IF NOT EXISTS program_v2_unique_program_v2
  ON program_v2 (policyCode, variant) WHERE policyCode IS NOT NULL AND variant IS NOT NULL;

-- Таблица со счётчиком номеров договоров в разрезе типов программ страхования
CREATE TABLE contract_number_sequence (
 lastId          BIGINT NOT NULL DEFAULT 0,
 programKind     VARCHAR(5) NOT NULL,
 UNIQUE (programKind)
);

--Предзаполнение таблицы со счётчиком номеров договоров в зависимости от текущего наполнения таблицы договоров.
--Для договоров типа КСП сделано фиксированное значение 6000, для предупреждения ошибок импорта договоров из быстрого старта
INSERT INTO contract_number_sequence (lastId, programKind)
SELECT CASE p2.type
    WHEN 'KSP' THEN 6000
    ELSE COUNT(ins.id)
  END,
       p2.type
FROM insurance ins
  INNER JOIN program_setting ps ON ps.id = ins.program_setting_id
  INNER JOIN program_v2 p2 ON p2.id = ps.program
GROUP BY p2.type