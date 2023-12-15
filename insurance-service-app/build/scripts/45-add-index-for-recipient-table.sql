set search_path to 'insurance_service';

CREATE INDEX IF NOT EXISTS insurance_2_recipient_tax_residence_idx
  ON insurance_2_recipient (tax_residence);