set search_path to 'insurance_service';

CREATE INDEX IF NOT EXISTS insurance_not_deleted_holder_idx
ON insurance(holder_id);

CREATE INDEX IF NOT EXISTS insurance_not_deleted_insured_idx
ON insurance(insured_id);