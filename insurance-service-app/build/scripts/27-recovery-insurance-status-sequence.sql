set search_path to 'insurance_service';

-- Ранее в скрипте 06-insurance.sql выполнялась вставка в таблицу insurance_status с указанием значений для поля id.
-- Это привело к тому, что значения sequence, связанной с полем id, "отстало" от реальных значений в таблице.
-- Скрипт ниже корректирует значение sequence, связанной с полем id таблицы insurance_status.
BEGIN;
  LOCK TABLE insurance_status IN EXCLUSIVE MODE;
  SELECT setval(pg_get_serial_sequence('insurance_status', 'id'), COALESCE((SELECT MAX(id)+1 FROM insurance_status), 1), false);
COMMIT;