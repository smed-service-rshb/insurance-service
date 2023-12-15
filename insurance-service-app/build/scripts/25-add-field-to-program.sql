set search_path to 'insurance_service';

ALTER TABLE program_v2 ADD COLUMN IF NOT EXISTS name_for_print VARCHAR(50);