set search_path to 'insurance_service';

INSERT INTO insurance_status(code, name) VALUES ('CLIENT_REFUSED', 'Отказ клиента') ON CONFLICT DO NOTHING;
INSERT INTO insurance_status(code, name) VALUES ('CANCELED_IN_HOLD_PERIOD', 'Расторгнут в период охлаждения') ON CONFLICT DO NOTHING;