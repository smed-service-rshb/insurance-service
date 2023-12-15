set search_path to 'insurance_service';

ALTER TABLE insurance_service.program_setting ADD column if not exists special_rate boolean NULL;
ALTER TABLE insurance_service.program_setting ADD column if not exists individual_rate boolean NULL;
ALTER TABLE insurance_service.program_setting ADD column if not exists special_rate_value numeric NULL;
ALTER TABLE insurance_service.insurance ADD column if not exists individual_rate boolean NULL;
ALTER TABLE insurance_service.insurance_aud ADD column if not exists individual_rate boolean NULL;
ALTER TABLE insurance_service.insurance ADD column if not exists set_rate_empoloyee_id bigint NULL;
ALTER TABLE insurance_service.insurance_aud ADD column if not exists set_rate_empoloyee_id bigint NULL;
ALTER TABLE insurance_service.insurance ADD column if not exists individual_rate_date date NULL;
ALTER TABLE insurance_service.insurance_aud ADD column if not exists individual_rate_date date NULL;