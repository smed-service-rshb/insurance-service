set search_path to 'insurance_service';

ALTER TABLE program_setting DROP COLUMN program_code;
ALTER TABLE program_setting DROP COLUMN program_tariff;

ALTER TABLE program_v2 ALTER COLUMN policyCode DROP NOT NULL;
ALTER TABLE program_v2 ADD column if not exists program_tariff VARCHAR(1);
ALTER TABLE program_v2 ALTER COLUMN insurance_program_code TYPE VARCHAR(1);

INSERT INTO program_v2(id, version, name, type, number, isactive, insurance_program_code, program_tariff, deleted)
VALUES (1, 0, 'Медсоветник. Индивидуальный', 'SMS', '001', true, 'K', 'B', false);

INSERT INTO program_v2(id, version, name, type, number, isactive, insurance_program_code, program_tariff, deleted)
VALUES (2, 0, 'Медсоветник. Семейный (1+1)', 'SMS', '001', true, 'K', 'A', false);

INSERT INTO program_v2(id, version, name, type, number, isactive, insurance_program_code, program_tariff, deleted)
VALUES (3, 0, 'Медсовтеник. Максимальный', 'SMS', '001', true, 'K', 'C', false);

INSERT INTO program_v2(id, version, name, type, number, isactive, insurance_program_code, program_tariff, deleted)
VALUES (4, 0, 'Медсоветник +. Демо-версия', 'SMS', '001', true, 'M', 'D', false);

INSERT INTO program_v2(id, version, name, type, number, isactive, insurance_program_code, program_tariff, deleted)
VALUES (5, 0, 'Медсоветник +. Оптимум', 'SMS', '001', true, 'M', 'E', false);

INSERT INTO program_v2(id, version, name, type, number, isactive, insurance_program_code, program_tariff, deleted)
VALUES (6, 0, 'Медсоветник +. Максимальный', 'SMS', '001', true, 'M', 'F', false);
