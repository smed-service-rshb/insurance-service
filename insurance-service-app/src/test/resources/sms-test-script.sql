-- Настройка связи "Разрешение - Права" (должно соответствовать insurance-service-app/build/scripts/03-insurance-service-permissions.sql)
insert into rights_to_permission(permission_id, right_id)
values ('view-contract', 'VIEW_CLIENT_CONTRACTS');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract', 'VIEW_CONTRACT_LIST_ALL');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract', 'VIEW_CONTRACT_LIST_RF_VSP');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract', 'VIEW_CONTRACT_LIST_VSP');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract', 'VIEW_CONTRACT_LIST_OWNER');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract', 'VIEW_CONTRACT_REQUIRED_UNDERWRITING');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract', 'CLIENT_VIEW_CONTRACT');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract', 'CLIENT_VIEW_CONTRACTS_LIST');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract-report', 'VIEW_CONTRACT_REPORT_ALL');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract-report', 'VIEW_CONTRACT_REPORT_RF_VSP');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract-report', 'VIEW_CONTRACT_REPORT_VSP');
insert into rights_to_permission(permission_id, right_id)
values ('view-contract-report', 'VIEW_CONTRACT_REPORT_OWNER');
insert into rights_to_permission(permission_id, right_id)
values ('view-dict', 'EDIT_PRODUCT_SETTINGS');
insert into rights_to_permission(permission_id, right_id)
values ('view-dict', 'CREATE_CONTRACT');
insert into rights_to_permission(permission_id, right_id)
values ('access-clients-info', 'VIEW_CLIENT');
insert into rights_to_permission(permission_id, right_id)
values ('create-or-edit-clients', 'VIEW_CLIENT');
insert into rights_to_permission(permission_id, right_id)
values ('create-or-edit-clients', 'EXECUTE_EDIT_CLIENT');
insert into rights_to_permission(permission_id, right_id)
values ('create-or-edit-clients', 'EXECUTE_CREATE_CLIENT');
insert into rights_to_permission(permission_id, right_id)
values ('inspect-edit-clients', 'INSPECT_EDIT_CLIENT');
insert into rights_to_permission(permission_id, right_id)
values ('create-contract', 'CREATE_CONTRACT');
insert into rights_to_permission(permission_id, right_id)
values ('create-contract', 'CREATE_CONTRACT_IN_CALL_CENTER');
insert into rights_to_permission(permission_id, right_id)
values ('view-template-content', 'EDIT_CLIENT_TEMPLATES');
insert into rights_to_permission(permission_id, right_id)
values ('view-template-content', 'VIEW_CLIENT_TEMPLATES_LIST');
insert into rights_to_permission(permission_id, right_id)
values ('view-template-content', 'VIEW_PERSONAL_OFFICE');
insert into rights_to_permission(permission_id, right_id)
values ('client-view-contract', 'CLIENT_VIEW_CONTRACT');
insert into rights_to_permission(permission_id, right_id)
values ('client-view-contract', 'VIEW_PERSONAL_OFFICE');
insert into rights_to_permission(permission_id, right_id)
values ('view-client-template', 'VIEW_CLIENT_TEMPLATES_LIST');
insert into rights_to_permission(permission_id, right_id)
values ('view-client-template', 'VIEW_PERSONAL_OFFICE');
insert into rights_to_permission(permission_id, right_id)
values ('update-client-request-attachment', 'CLIENT_REQUEST_CREATE');
insert into rights_to_permission(permission_id, right_id)
values ('update-client-request-attachment', 'CLIENT_REQUEST_PROCESSING');


-- Примеры тестовых сегментов программ страхования
INSERT INTO segment(id, version, code, name, isactive, deleted)
VALUES (1, 0, 'retail', 'Массовый', true, false);
INSERT INTO segment(id, version, code, name, isactive, deleted)
VALUES (2, 0, 'vip', 'Премиальный', true, false);

-- Примеры тестовых программ страхования
INSERT INTO program_v2(id, version, name, type, number, isactive, insurance_program_code, program_tariff, deleted)
VALUES (1, 0, 'Медсоветник. Индивидуальный', 'SMS', '001', true, 'K', 'B', false);

INSERT INTO program_related_group(programId, groupcode)
VALUES (1, 'СМС+ Медсервис');

INSERT INTO program_related_offices(programId, officeid)
VALUES (1, 2);

insert into audit_envers_info (id, timestamp)
VALUES (0, datediff('ms', '1970-01-01', now()));

-- Примеры тестовых параметров программ страхования
INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, insuranceamount, tariff,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, guarantee_level,
                            deleted)
VALUES (1, 0, (SELECT ID FROM program_v2 where name = 'Медсоветник. Индивидуальный'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 1, 1, 100,
        100, 200, 'BY_FORMULA', 2.1, null, null, 100,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, 75.5,
        false);

INSERT INTO insurance_status(id, code, name)
VALUES (1, 'MADE', 'Оформлен');
INSERT INTO insurance_status_AUD(id, code, name, REV, REVTYPE)
VALUES (1, 'MADE', 'Оформлен', 0, 0);
INSERT INTO insurance_status(id, code, name)
VALUES (2, 'CRM_IMPORTED', 'Оформлен');
INSERT INTO insurance_status_AUD(id, code, name, REV, REVTYPE)
VALUES (2, 'CRM_IMPORTED', 'Оформлен', 0, 0);

-- Параметры тестовых пользователей
INSERT INTO clients(id, surname, firstname, middlename, birth_date, gender, birth_place,
                    resident, resident_rf, tax_residence, tax_payer_number, public_official_status,
                    foreign_public_official_type, russian_public_official_type, relations,
                    public_official_position, public_official_name_and_position,
                    beneficial_owner, business_relations, activities_goal, business_relations_goal,
                    risk_level_desc, business_reputation, financial_stability, finances_source,
                    personal_data_consent, inn, snils, license_driver, sign_ipdl,
                    sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner,
                    has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level,
                    latin_name, code_word, status, email, agreements, nationalities,
                    registration_date, cache_save_time, last_update_date)
VALUES (1, 'Иванов', 'Иван', 'Иванович', TIMESTAMP '1986-03-30', 'MALE', 'СССР',
        'russian', true, 'RUSSIAN', '1234567891234', 'NONE',
        null, null, null,
        'Директор', 'Иванов Иван Иванович, Директор',
        null, null, null, null,
        null, null, 'STABLE', 'Бизнес',
        true, '123456789123', '12345678910', null, false,
        false, false, false, false,
        false, 'NOT_BANKRUPT', '', 'NORMAL',
        'IVANOV IVAN IVANOVICH', 'someword', 'STATUS-OK', 'ivanov@example.org', null, null,
        TIMESTAMP '2018-01-01', CURRENT_DATE, TIMESTAMP '2018-01-01');

INSERT INTO clients(id, surname, firstname, middlename, birth_date, gender, birth_place,
                    resident, resident_rf, tax_residence, tax_payer_number, public_official_status,
                    foreign_public_official_type, russian_public_official_type, relations,
                    public_official_position, public_official_name_and_position,
                    beneficial_owner, business_relations, activities_goal, business_relations_goal,
                    risk_level_desc, business_reputation, financial_stability, finances_source,
                    personal_data_consent, inn, snils, license_driver, sign_ipdl,
                    sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner,
                    has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level,
                    latin_name, code_word, status, email, agreements, nationalities,
                    registration_date, cache_save_time, last_update_date)
VALUES (2, 'Петров', 'Пётр', 'Петрович', TIMESTAMP '1989-01-01', 'MALE', 'СССР',
        'russian', true, 'RUSSIAN', '223456789123', 'NONE',
        null, null, null,
        'Дворник', 'Петров Пётр Петрович, Дворник',
        null, null, null, null,
        null, null, 'STABLE', 'Бизнес',
        true, '123456789123', '12345678912', null, false,
        false, false, false, false,
        false, 'NOT_BANKRUPT', '', 'NORMAL',
        'PETROV PETR PETROVICH', 'someword', 'STATUS-OK', 'petrov@example.com', null, null,
        TIMESTAMP '2018-01-01', CURRENT_DATE, TIMESTAMP '2018-01-01');

INSERT INTO clients(id, surname, firstname, middlename, birth_date, gender, birth_place,
                    resident, resident_rf, tax_residence, tax_payer_number, public_official_status,
                    foreign_public_official_type, russian_public_official_type, relations,
                    public_official_position, public_official_name_and_position,
                    beneficial_owner, business_relations, activities_goal, business_relations_goal,
                    risk_level_desc, business_reputation, financial_stability, finances_source,
                    personal_data_consent, inn, snils, license_driver, sign_ipdl,
                    sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner,
                    has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level,
                    latin_name, code_word, status, email, agreements, nationalities,
                    registration_date, cache_save_time, last_update_date)
VALUES (3, 'Семёнов', 'Семён', 'Семёнович', TIMESTAMP '1975-01-01', 'MALE', 'СССР',
        'russian', true, 'RUSSIAN', '3234567891234', 'NONE',
        null, null, null,
        'Секретарь', 'Семёнов Семён Семёнович, Секретарь',
        null, null, null, null,
        null, null, 'STABLE', 'Бизнес',
        true, '123456789123', '12345678910', null, false,
        false, false, false, false,
        false, 'NOT_BANKRUPT', '', 'NORMAL',
        'SEMENOV SEMEN SEMENOVICH', 'someword', 'STATUS-OK', 'semenov@example.org', null, null,
        TIMESTAMP '2018-01-01', CURRENT_DATE, TIMESTAMP '2018-01-01');



INSERT INTO clients(id, surname, firstname, middlename, birth_date, gender, birth_place,
                    resident, resident_rf, tax_residence, tax_payer_number, public_official_status,
                    foreign_public_official_type, russian_public_official_type, relations,
                    public_official_position, public_official_name_and_position,
                    beneficial_owner, business_relations, activities_goal, business_relations_goal,
                    risk_level_desc, business_reputation, financial_stability, finances_source,
                    personal_data_consent, inn, snils, license_driver, sign_ipdl,
                    sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner,
                    has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level,
                    latin_name, code_word, status, email, agreements, nationalities,
                    registration_date, cache_save_time, last_update_date)
VALUES (4, 'Иванов', 'Иван', 'Иванович', TIMESTAMP '1986-03-30', 'MALE', 'СССР',
        'russian', true, 'RUSSIAN', '1234567891234', 'NONE',
        null, null, null,
        'Директор', 'Иванов Иван Иванович, Директор',
        null, null, null, null,
        null, null, 'STABLE', 'Бизнес',
        true, '123456789123', '12345678910', null, false,
        false, false, false, false,
        false, 'NOT_BANKRUPT', '', 'NORMAL',
        'IVANOV IVAN IVANOVICH', 'someword', 'STATUS-OK', 'ivanov@example.org', null, null,
        TIMESTAMP '2018-01-01', CURRENT_DATE, TIMESTAMP '2018-01-01');

INSERT INTO clients(id, surname, firstname, middlename, birth_date, gender, birth_place,
                    resident, resident_rf, tax_residence, tax_payer_number, public_official_status,
                    foreign_public_official_type, russian_public_official_type, relations,
                    public_official_position, public_official_name_and_position,
                    beneficial_owner, business_relations, activities_goal, business_relations_goal,
                    risk_level_desc, business_reputation, financial_stability, finances_source,
                    personal_data_consent, inn, snils, license_driver, sign_ipdl,
                    sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner,
                    has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level,
                    latin_name, code_word, status, email, agreements, nationalities,
                    registration_date, cache_save_time, last_update_date)
VALUES (9, 'Иванов', 'Иван', 'Петрович', TIMESTAMP '1986-03-30', 'MALE', 'СССР',
        'russian', true, 'FOREIGN', '1234567891234', 'NONE',
        null, null, null,
        'Директор', 'Иванов Иван Петрович, Клиент',
        null, null, null, null,
        'Низкий', null, 'STABLE', 'Бизнес',
        true, '1234567891234', '12345678910', null, false,
        false, false, false, false,
        false, 'NOT_BANKRUPT', '', 'NORMAL',
        'IVANOV IVAN PETROVICH', 'someword', 'STATUS-OK', 'ivanov@example.org', null, null,
        TIMESTAMP '2018-01-01', CURRENT_DATE, TIMESTAMP '2018-01-01');

INSERT INTO client_check(id, check_type, check_result, client_id, comment, creation_date, update_id)
VALUES (6, 'TERRORIST', TRUE, 1, null, TIMESTAMP '2019-01-15 10:51:26.523000', -1);

INSERT INTO client_check(id, check_type, check_result, client_id, comment, creation_date, update_id)
VALUES (7, 'BLOCKAGE', TRUE, 1, null, TIMESTAMP '2019-01-15 10:50:26.523000', 2);

INSERT INTO client_check(id, check_type, check_result, client_id, comment, creation_date, update_id)
VALUES (8, 'TERRORIST', TRUE, 3, null, TIMESTAMP '2019-01-15 10:51:26.523000', -1);

INSERT INTO client_check(id, check_type, check_result, client_id, comment, creation_date, update_id)
VALUES (9, 'INVALID_IDENTITY_DOC', TRUE, 1, null, TIMESTAMP '2019-01-15 10:50:26.523000', 1);

INSERT INTO client_check(id, check_type, check_result, client_id, comment, creation_date, update_id)
VALUES (10, 'INVALID_IDENTITY_DOC', TRUE, 1, null, TIMESTAMP '2019-01-15 10:50:29.523000', 2);

INSERT INTO client_check(id, check_type, check_result, client_id, comment, creation_date, update_id)
VALUES (11, 'TERRORIST', TRUE, 4, null, TIMESTAMP '2019-01-15 10:52:26.527000', -1);

INSERT INTO client_check(id, check_type, check_result, client_id, comment, creation_date, update_id)
VALUES (12, 'INVALID_IDENTITY_DOC', TRUE, 2, null, TIMESTAMP '2019-01-15 10:32:28.523000', 3);


INSERT INTO addresses_for_client(id, address_type, country, region, area, city, locality, street,
                                 house, construction, housing, apartment, index, registration_period_start,
                                 registration_period_end, client_id)
VALUES (1, 'RESIDENCE', 'RUSSIA', null, null, null, null, null,
        null, null, null, null, null, null,
        null, 1);
INSERT INTO addresses_for_client(id, address_type, country, region, area, city, locality, street,
                                 house, construction, housing, apartment, index, registration_period_start,
                                 registration_period_end, client_id)
VALUES (2, 'RESIDENCE', 'RUSSIA', null, null, null, null, null,
        null, null, null, null, null, null,
        null, 1);
-- --
INSERT INTO addresses_for_client(id, address_type, country, region, area, city, locality, street,
                                 house, construction, housing, apartment, index, registration_period_start,
                                 registration_period_end, client_id)
VALUES (3, 'RESIDENCE', 'RUSSIA', null, null, null, null, null,
        null, null, null, null, null, null,
        null, 2);
-- --
INSERT INTO addresses_for_client(id, address_type, country, region, area, city, locality, street,
                                 house, construction, housing, apartment, index, registration_period_start,
                                 registration_period_end, client_id)
VALUES (4, 'REGISTRATION', 'RUSSIA', null, null, null, null, null,
        null, null, null, null, null, null,
        null, 3);
-- --
INSERT INTO addresses_for_client(id, address_type, country, region, area, city, locality, street,
                                 house, construction, housing, apartment, index, registration_period_start,
                                 registration_period_end, client_id)
VALUES (5, 'RESIDENCE', 'RUSSIA', null, null, null, null, null,
        null, null, null, null, null, null,
        null, 4);
INSERT INTO addresses_for_client(id, address_type, country, region, area, city, locality, street,
                                 house, construction, housing, apartment, index, registration_period_start,
                                 registration_period_end, client_id)
VALUES (6, 'RESIDENCE', 'RUSSIA', null, null, null, null, null,
        null, null, null, null, null, null,
        null, 9);

INSERT INTO documents_for_client(id, doc_type, doc_series, doc_number, issued_by, issued_date,
                                 is_active, is_main, division_code, issued_end_date, is_valid_document,
                                 stay_start_date, stay_end_date, client_id, scan_id, is_identity,
                                 doc_name)
VALUES (1, 'PASSPORT_RF', '1234', '123456', null, null,
        true, true, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 1, null, true,
        'паспорт');

INSERT INTO documents_for_client(id, doc_type, doc_series, doc_number, issued_by, issued_date,
                                 is_active, is_main, division_code, issued_end_date, is_valid_document,
                                 stay_start_date, stay_end_date, client_id, scan_id, is_identity,
                                 doc_name)
VALUES (2, 'PASSPORT_RF', '2345', '234567', null, null,
        true, true, 123456, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 2, null, true,
        'паспорт');


INSERT INTO documents_for_client(id, doc_type, doc_series, doc_number, issued_by, issued_date,
                                 is_active, is_main, division_code, issued_end_date, is_valid_document,
                                 stay_start_date, stay_end_date, client_id, scan_id, is_identity,
                                 doc_name)
VALUES (3, 'PASSPORT_RF', '3456', '345678', null, null,
        false, true, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 3, null, true,
        'паспорт');

INSERT INTO documents_for_client(id, doc_type, doc_series, doc_number, issued_by, issued_date,
                                 is_active, is_main, division_code, issued_end_date, is_valid_document,
                                 stay_start_date, stay_end_date, client_id, scan_id, is_identity,
                                 doc_name)
VALUES (4, 'PASSPORT_RF', '4321', '654321', null, null,
        true, true, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 4, null, true,
        'паспорт');

INSERT INTO documents_for_client(id, doc_type, doc_series, doc_number, issued_by, issued_date,
                                 is_active, is_main, division_code, issued_end_date, is_valid_document,
                                 stay_start_date, stay_end_date, client_id, scan_id, is_identity,
                                 doc_name)
VALUES (5, 'PASSPORT_RF', '5432', '765432', null, null,
        true, false, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 1, null, true,
        'паспорт');

INSERT INTO documents_for_client(id, doc_type, doc_series, doc_number, issued_by, issued_date,
                                 is_active, is_main, division_code, issued_end_date, is_valid_document,
                                 stay_start_date, stay_end_date, client_id, scan_id, is_identity,
                                 doc_name)
VALUES (6, 'PASSPORT_RF', '5499', '769999', null, null,
        true, true, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 9, null, true,
        'паспорт');

INSERT INTO phones_for_client(id, phone_type, number, main, verified, notification, client_id)
VALUES (1, 'MOBILE', '79110000001', true, true, true, 1);
INSERT INTO phones_for_client(id, phone_type, number, main, verified, notification, client_id)
VALUES (2, 'MOBILE', '+791100000002', true, true, true, 2);
INSERT INTO phones_for_client(id, phone_type, number, main, verified, notification, client_id)
VALUES (3, 'MOBILE', '+791100000003', false, true, true, 3);
INSERT INTO phones_for_client(id, phone_type, number, main, verified, notification, client_id)
VALUES (4, 'MOBILE', '+791100000033', false, true, true, 3);
INSERT INTO phones_for_client(id, phone_type, number, main, verified, notification, client_id)
VALUES (5, 'MOBILE', '+791100000004', true, true, true, 4);
INSERT INTO phones_for_client(id, phone_type, number, main, verified, notification, client_id)
VALUES (6, 'MOBILE', '+791100000005', false, true, true, 4);
INSERT INTO phones_for_client(id, phone_type, number, main, verified, notification, client_id)
VALUES (9, 'MOBILE', '+79999999999', true, true, true, 9);

-- Примеры тестовых договоров страхования
insert into insurance (id, creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id,
                       employee_name, program_setting_id, version, deleted, holder_id, holder_version,
                       holder_equals_insured, insured_id, duration, start_date, currency, premium, rur_premium, amount,
                       rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm,
                       conclusiondate)
VALUES (1, TIMESTAMP '2018-12-12 12:12:12', 'KB12000001', 'Амурский РФ', '2310', 2, 9, 'Фёдоров Фёдор Фёдорович',
        1, 1, false, 1, 1, TRUE, 1, 3, TIMESTAMP '2018-11-27', 1, 1000, 1000, 10000, 10000, 'ONCE', 1, TRUE, 'YEAR', 10,
        TIMESTAMP '2018-12-12 12:12:12');

