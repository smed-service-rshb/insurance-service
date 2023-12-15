set search_path to 'insurance_service';

ALTER TABLE statements ADD COLUMN complete_status VARCHAR(15);

UPDATE statements
SET complete_status = 'FULL'
WHERE is_complete = true;

UPDATE statements
SET complete_status = 'NOT_FULL'
WHERE is_complete = false;

ALTER TABLE statements DROP COLUMN is_complete;

