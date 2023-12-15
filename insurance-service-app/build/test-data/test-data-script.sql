-- В данном файле находятся скрипты, которые заполняют БД тестовыми данными для демонстрационных целей.
-- При разворачивании приложения на промышленном стенде данных сприты выполняться не должны.
-- В данный файл нельзя добалять скрипты наполнения таблиц данными, которые требуются по ТЗ. Эти скприты
-- должны быть описаны в других файлах (например, в файле, где описывается структура таблицы).

set search_path to 'insurance_service';

INSERT INTO program(version, name, code, minAge, maxAge) VALUES (0, 'Выбери здоровье плюс', '01', 18, 54);
INSERT INTO program(version, name, code, minAge, maxAge) VALUES (0, 'Выбери здоровье классик', '02', 18, 54);
INSERT INTO program(version, name, code, minAge, maxAge) VALUES (0, 'Выбери здоровье', '03', 55, 64);

INSERT INTO contract (creation_date, contract_number, branch_name, subdivision_name, subdivision_Id,  employee_id, employee_personnel_number, employee_name, surname, first_name, middle_name, birth_date, email, phone_number, program, version, deleted)
VALUES (TIMESTAMP '2018-01-01 12:12:12', '23К010110000001', 'Амурский РФ', '2310', 2002310, 9, '10009', 'Фёдоров Фёдор Фёдорович', 'Лисичкин', 'Михаил', 'Алексеевич', '1978-10-06', 'test_1@example.com', '+79999999999', 1, 1, false);
INSERT INTO contract (creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id, employee_personnel_number, employee_name, surname, first_name, middle_name, birth_date, email, phone_number, program, version, deleted)
VALUES (TIMESTAMP '2018-01-02 12:12:12', '23К010110000002', 'Амурский РФ', '2310', 2002310, 9, '10009', 'Фёдоров Фёдор Фёдорович', 'Лисичкин', 'Михаил', 'Алексеевич', '1978-10-06', 'test_2@example.com', '+79999999992', 2, 1, false);
INSERT INTO contract (creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id, employee_personnel_number, employee_name, surname, first_name, middle_name, birth_date, email, phone_number, program, version, deleted)
VALUES (TIMESTAMP '2018-01-03 12:12:12', '23К010110000003', 'Амурский РФ', '2310', 2002310, 9, '10009', 'Фёдоров Фёдор Фёдорович', 'Лисичкин', 'Михаил', 'Алексеевич', '1978-10-06', 'test_3@example.com', '+79999999993', 3, 1, false);
INSERT INTO contract (creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id, employee_personnel_number, employee_name, surname, first_name, middle_name, birth_date, email, phone_number, program, version, deleted)
VALUES (TIMESTAMP '2018-01-04 12:12:12', '23К010110000004', 'Амурский РФ', '2310', 2002310, 9, '10009', 'Фёдоров Фёдор Фёдорович', 'Удаленный', 'Удаленный', 'Удаленный', '1978-10-06', 'deleted@example.com', '+79999999993', 2, 1, true);

-- Наполнение данными справочника продуктов страхования
INSERT INTO program_v2(version, name, type, number, policycode, variant, coolingperiod, segment, isactive, deleted)
VALUES (0, 'Классический ИСЖ', 'ISJ', '001', 'Я01', '01', 14, null, true, false);
INSERT INTO program_v2(version, name, type, number, policycode, variant, coolingperiod, segment, isactive, deleted)
VALUES (0, 'Купонный ИСЖ', 'ISJ', '002', 'Ю99', '01', 14, null, true, false);

-- Наполнение данными справочника параметров программ страхования
INSERT INTO program_setting(version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (0, (SELECT ID FROM program_v2 where name = 'Классический ИСЖ'), DATE('2018-08-01'), DATE('2999-12-31'), true,
        1, 5, 'YEAR', 1, 0, 100000,
        0, 50000, 'FIXED', null, null,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

INSERT INTO program_setting(version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (0, (SELECT id FROM program_v2 WHERE name = 'Купонный ИСЖ'), DATE('2018-08-01'), DATE('2999-12-31'), true,
        1, 5, 'YEAR', 1, 0, 1000000,
        0, 50000, 'FIXED', null, null,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

INSERT INTO document_template_2_program_setting(documentTemplateId, programSettingId) VALUES ('4f2434ae-5eb1-4b10-8f83-2cbcddfdbf94', 1);
INSERT INTO document_template_2_program_setting(documentTemplateId, programSettingId) VALUES ('4f2434ae-5eb1-4b10-8f83-2cbcddfdbf94', 2);

INSERT INTO attachments(
    id, kind, documenttype, createdate, contract, filename, comment,
    owner, verified, deleted, content)
VALUES (5, 'INSURANCE_CONTRACT', null, TIMESTAMP '2018-01-02', null, 'test4.xml', null,
        null, false, false, convert_to('<?xml version="1.0" encoding="UTF-8"?>'::text, 'utf8'));

-- Тестовый клиент (интеграц. тесты, клиент заведен в auth-service)
INSERT INTO insurance_service.clients(id, surname, firstname, middlename, birth_date, gender, birth_place, birth_country, resident, resident_rf, tax_residence, tax_payer_number, public_official_status, foreign_public_official_type, russian_public_official_type, relations, public_official_position, public_official_name_and_position, beneficial_owner, business_relations, activities_goal, business_relations_goal, risk_level_desc, business_reputation, financial_stability, finances_source, personal_data_consent, inn, snils, license_driver, sign_ipdl, sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner, has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level, latin_name, code_word, status, email, agreements, nationalities, registration_date, cache_save_time, last_update_date, citizenship_country)
    VALUES(1, 'Сергеев', 'Андрей', 'Анатольевич', '1987-07-10', 'MALE', null, null, null, false, 'RUSSIAN', null, 'NONE', null, null, null, null, null, null, null, null, null, null, null, 'STABLE', null, true, null, null, null, true, true, true, true, true, true, 'NOT_BANKRUPT', null, null, null, null, null, null, null, null, '2018-11-11', '2018-11-11', '2018-11-11', null)
;
INSERT INTO insurance_service.phones_for_client(id, phone_type, number, main, verified, notification, client_id)
    VALUES(1, 'MOBILE', '+79212365557', true, true, true, 1)
;
INSERT INTO documents_for_client(
    id, doc_type, doc_series, doc_number, issued_by, issued_date,
    is_active, is_main, division_code, issued_end_date, is_valid_document,
    stay_start_date, stay_end_date, client_id, scan_id, is_identity,
    doc_name)
VALUES (1, 'PASSPORT_RF', '4321', '654321', null, null,
        true, true, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 1, null, true,
        'паспорт');

INSERT INTO insurance_service.insurance (id, creation_date,     contract_number,   branch_name,  subdivision_name, subdivision_Id, employee_id,  employee_name,             program_setting_id, version, deleted, holder_id, holder_version, holder_equals_insured, insured_id, duration, start_date,              currency, premium, rur_premium, amount, rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm, conclusiondate )
VALUES (1, TIMESTAMP '2018-12-10 10:12:10', '23К010110000000', 'Амурский РФ', '2310',           2002310,              9,            'Сергеев Андрей Анатольевич', 2,                  1,       false,   1,         1,              TRUE, 1,                  3,        TIMESTAMP '2018-12-27',   1,        1000,    1000,        10000,  10000,      'ONCE',       1,         TRUE,                    'YEAR', 10, TIMESTAMP '2018-12-12 12:12:12');
;
INSERT INTO insurance_service.contract(id, version, creation_date, contract_number, surname, first_name, middle_name, birth_date, email, phone_number, employee_id, employee_personnel_number, employee_name, branch_name, subdivision_name, program, subdivision_id, deleted, code, cashier_id, cashier_personnel_number, cashier_name, cashier_branch_name, cashier_office_name)
    VALUES(101, 1, '2018-12-10 12:12:12', '23К010110000001', 'Сергеев', 'Андрей', 'Анатольевич', '1987-07-10', 'sergeev@example.ru', '+79212365557', 9, '1234234', 'Соколов И.Е.', 'Амурский РФ', '2310', (SELECT ID from insurance_service.program where code = '01'), 2002310, false, 'code', 0, '1', '+79999999999', 'Амурский РФ', '2310')
;

-- Тестовый клиент (интеграц. тесты, клиент не заведен в auth-service)
INSERT INTO insurance_service.clients(id, surname, firstname, middlename, birth_date, gender, birth_place, birth_country, resident, resident_rf, tax_residence, tax_payer_number, public_official_status, foreign_public_official_type, russian_public_official_type, relations, public_official_position, public_official_name_and_position, beneficial_owner, business_relations, activities_goal, business_relations_goal, risk_level_desc, business_reputation, financial_stability, finances_source, personal_data_consent, inn, snils, license_driver, sign_ipdl, sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner, has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level, latin_name, code_word, status, email, agreements, nationalities, registration_date, cache_save_time, last_update_date, citizenship_country)
    VALUES(2, 'Вавилов', 'Илья',   'Сергеевич',   '1987-11-11', 'MALE', null, null, null, false, 'RUSSIAN', null, 'NONE', null, null, null, null, null, null, null, null, null, null, null, 'STABLE', null, true, null, null, null, true, true, true, true, true, true, 'NOT_BANKRUPT', null, null, null, null, null, null, null, null, '2018-11-11', '2018-11-11', '2018-11-11', null)
;
INSERT INTO insurance_service.phones_for_client(id, phone_type, number, main, verified, notification, client_id)
    VALUES(2, 'MOBILE', '+79212365556', true, true, true, 2)
;
INSERT INTO documents_for_client(
    id, doc_type, doc_series, doc_number, issued_by, issued_date,
    is_active, is_main, division_code, issued_end_date, is_valid_document,
    stay_start_date, stay_end_date, client_id, scan_id, is_identity,
    doc_name)
VALUES (2, 'PASSPORT_RF', '1234', '654321', null, null,
        true, true, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 2, null, true,
        'паспорт');

INSERT INTO insurance_service.insurance (id, creation_date,     contract_number,   branch_name,  subdivision_name, subdivision_Id, employee_id,  employee_name,             program_setting_id, version, deleted, holder_id, holder_version, holder_equals_insured, insured_id, duration, start_date,              currency, premium, rur_premium, amount, rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm, conclusiondate )
VALUES (2, TIMESTAMP '2018-12-12 12:12:12', '23К010110000001', 'Амурский РФ', '2310',           2002310,              9,            'Вавилов Илья Сергеевич', 2,                  1,       false,   2,         1,              TRUE, 1,                  3,        TIMESTAMP '2018-12-27',   1,        1000,    1000,        10000,  10000,      'ONCE',       1,         TRUE,                    'YEAR', 10, TIMESTAMP '2018-12-12 12:12:12');
;
INSERT INTO insurance_service.contract(id, version, creation_date, contract_number, surname, first_name, middle_name, birth_date, email, phone_number, employee_id, employee_personnel_number, employee_name, branch_name, subdivision_name, program, subdivision_id, deleted, code, cashier_id, cashier_personnel_number, cashier_name, cashier_branch_name, cashier_office_name)
    VALUES(102, 1, '2018-01-01 12:12:12', '23К010110000001', 'Вавилов', 'Илья', 'Сергеевич', '1987-11-11', 'vavilov@example.ru', '+79212365556', 9, '1234234', 'Соколов И.Е.', 'Амурский РФ', '2310', (SELECT ID from insurance_service.program where code = '01'), 2002310, false, 'code', 0, '1', '+79999999999', 'Амурский РФ', '2310')
;

COMMIT;