set search_path to 'insurance_service';

INSERT INTO insurance_status(code, name) VALUES ('REVOKED_REPLACEMENT', 'Аннулирование по замене') ON CONFLICT DO NOTHING;

ALTER TABLE insurance ADD COLUMN IF NOT EXISTS parent_insurance BIGINT NULL;
ALTER TABLE insurance ADD COLUMN IF NOT EXISTS initial_contract_number  varchar(255) NULL;