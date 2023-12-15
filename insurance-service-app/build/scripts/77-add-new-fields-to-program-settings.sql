set search_path to 'insurance_service';

ALTER TABLE program_v2 ADD COLUMN comulation INTEGER;
ALTER TABLE program_v2 ADD COLUMN insurance_program_code VARCHAR(20);
ALTER TABLE program_v2 ADD COLUMN insurance_char_code VARCHAR(2);