set search_path to 'insurance_service';

INSERT INTO insurance_status(id, code, name)
VALUES (5, 'DRAFT', 'Черновик')
ON CONFLICT DO NOTHING;

