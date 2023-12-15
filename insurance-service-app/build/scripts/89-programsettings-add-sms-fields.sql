set search_path to 'insurance_service';

ALTER TABLE insurance_service.program_setting ADD column if not exists program_code VARCHAR(1);
ALTER TABLE insurance_service.program_setting ADD column if not exists program_tariff VARCHAR(1);