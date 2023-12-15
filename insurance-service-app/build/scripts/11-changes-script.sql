-- В данном файле находятся скрипты, модифицируют структуру БД и которые можно выполнять несколько раз

set search_path to 'insurance_service';

ALTER TABLE insurance_2_recipient ADD COLUMN IF NOT EXISTS birth_country VARCHAR(100);
ALTER TABLE clients ADD COLUMN IF NOT EXISTS citizenship_country CHARACTER VARYING(100);

ALTER TABLE program_setting ADD COLUMN IF NOT EXISTS guarantee_level NUMERIC NULL;

ALTER TABLE risk ALTER COLUMN program_kind TYPE varchar(4) USING program_kind::varchar(4);
ALTER TABLE program_v2 ALTER COLUMN type TYPE varchar(4) USING type::varchar(4);

ALTER TABLE risk ADD COLUMN IF NOT EXISTS payment_method varchar(20) NULL;