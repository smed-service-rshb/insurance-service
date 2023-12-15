set search_path to 'insurance_service';
--Дата окончания действия
ALTER TABLE insurance ADD column if not exists end_date DATE NULL;
--Фиксированная дата начала действия договора
ALTER TABLE insurance ADD column if not exists fixed_start_date DATE NULL;
