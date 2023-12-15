set search_path to 'insurance_service';

ALTER TABLE insurance_service.risk_setting
  ADD COLUMN IF NOT EXISTS sortPriority NUMERIC;

