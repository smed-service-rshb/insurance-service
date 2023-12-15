set search_path to 'insurance_service';

-- добавление статичной даты начала оформления договора
ALTER TABLE insurance_service.program_setting ADD COLUMN IF NOT EXISTS staticDate TIMESTAMP;
