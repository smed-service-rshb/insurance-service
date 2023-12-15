set search_path to 'insurance_service';

ALTER TABLE insurance_service.acquiring_info ALTER COLUMN acquiring_program_id DROP NOT NULL;