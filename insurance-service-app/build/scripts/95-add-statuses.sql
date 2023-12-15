set search_path to 'insurance_service';

INSERT INTO insurance_status(id, code, name)
VALUES (3, 'PAYED', 'Оплачен')
ON CONFLICT DO NOTHING;
INSERT INTO insurance_status(id, code, name)
VALUES (4, 'CANCELED', 'Расторгнут')
ON CONFLICT DO NOTHING;
