set search_path to 'insurance_service';

CREATE INDEX IF NOT EXISTS client_check_check_type_idx
ON client_check (check_type);

CREATE INDEX IF NOT EXISTS client_check_check_result_idx
ON client_check (check_result);