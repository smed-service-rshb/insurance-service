set search_path to 'insurance_service';
--Дата оформления договора
ALTER TABLE insurance ADD column if not exists conclusionDate TIMESTAMP NULL;