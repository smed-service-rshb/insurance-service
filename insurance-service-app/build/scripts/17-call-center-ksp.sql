set search_path to 'insurance_service';

ALTER TABLE insurance_service.insurance ADD COLUMN IF NOT EXISTS code VARCHAR(6);
ALTER TABLE insurance_service.insurance ADD COLUMN IF NOT EXISTS call_center_employee_id bigint NULL;
ALTER TABLE insurance_service.insurance ADD COLUMN IF NOT EXISTS call_center_employee_name VARCHAR(256);
ALTER TABLE insurance_service.insurance ADD COLUMN IF NOT EXISTS call_center_employee_number VARCHAR(20);