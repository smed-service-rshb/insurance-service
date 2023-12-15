set search_path to 'insurance_service';
--Дата окончания действия
ALTER TABLE insurance ADD column if not exists call_center_branch_name VARCHAR(255) NULL;
--Фиксированная дата начала действия договора
ALTER TABLE insurance ADD column if not exists call_center_subdivision_name VARCHAR(255) NULL;
