set search_path to 'insurance_service';

INSERT INTO insurance_status(code, name) VALUES ('MADE_NOT_COMPLETED', 'Оформление не завершено') ON CONFLICT DO NOTHING;