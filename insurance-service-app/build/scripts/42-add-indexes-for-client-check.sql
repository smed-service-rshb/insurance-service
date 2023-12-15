set search_path to 'insurance_service';

CREATE INDEX IF NOT EXISTS client_check_creation_date_idx
ON client_check (creation_date);