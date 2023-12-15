set search_path to 'insurance_service';

DELETE FROM insurance_status;

INSERT INTO insurance_status(id, code, name) VALUES (1, 'MADE', 'Оформлен');
INSERT INTO insurance_status(id, code, name) VALUES (2, 'CRM_IMPORTED', 'Выгружен в CRM');