set search_path to 'insurance_service';

ALTER TABLE insurance_service.request ALTER COLUMN program_id DROP NOT NULL;
ALTER TABLE insurance_service.request ALTER COLUMN insurance_id DROP NOT NULL;