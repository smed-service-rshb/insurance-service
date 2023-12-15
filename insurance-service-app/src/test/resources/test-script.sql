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
INSERT INTO program_v2(id, version, name, type, number, policycode, variant, coolingperiod, segment, isactive, deleted)
VALUES (1, 0, 'Классический ИСЖ', 'ISJ', '001', 'Я01', '01', 14, null, true, false);
INSERT INTO program_v2(id, version, name, name_for_print, type, number, policycode, variant, coolingperiod, segment,
                       isactive, deleted,
                       relatedOfficeFilterType, relatedEmployeeGroupFilterType)
VALUES (2, 0, 'Купонный ИСЖ', 'Купонный ИСЖ', 'ISJ', '002', 'Ю99', '01', 14, null, true, false, 'INCLUDE_ALL',
        'INCLUDE_ALL');
INSERT INTO program_v2(id, version, name, type, number, policycode, variant, coolingperiod, segment, isactive, deleted,
                       relatedOfficeFilterType, relatedEmployeeGroupFilterType)
VALUES (3, 0, 'Упрощённый НСЖ', 'NSJ', '003', 'Ы99', '01', 14, null, true, false, 'INCLUDE', 'INCLUDE');
INSERT INTO program_v2(id, version, name, type, number, policycode, variant, coolingperiod, segment, isactive, deleted,
                       relatedOfficeFilterType, relatedEmployeeGroupFilterType)
VALUES (4, 0, 'НСЖ', 'NSJ', '004', 'П99', '01', 14, null, true, false, 'EXCLUDE_ALL', 'EXCLUDE_ALL');
INSERT INTO program_v2(id, version, name, type, number, policycode, variant, coolingperiod, segment, isactive, deleted,
                       relatedOfficeFilterType, relatedEmployeeGroupFilterType)
VALUES (5, 0, 'НСЖ 2018', 'NSJ', '005', 'К99', '01', 14, null, true, false, 'EXCLUDE', 'EXCLUDE');

INSERT INTO program_v2(id, version, name, type, number, policycode, variant, coolingperiod, segment, isactive, deleted,
                       relatedOfficeFilterType, relatedEmployeeGroupFilterType)
VALUES (6, 0, 'Рента', 'RENT', '006', 'Р99', '01', 14, null, true, false, 'INCLUDE_ALL', 'INCLUDE_ALL');

INSERT INTO program_v2(id, version, name, name_for_print, type, number, policycode, variant, coolingperiod, segment,
                       isactive, deleted,
                       relatedOfficeFilterType, relatedEmployeeGroupFilterType)
VALUES (7, 0, 'Коробка', 'Коробка', 'KSP', '007', 'K99', '01', 14, null, true, false, 'INCLUDE_ALL', 'INCLUDE_ALL');

INSERT INTO program_v2(id, version, name, type, number, policycode, variant, coolingperiod, segment, isactive, deleted,
                       relatedOfficeFilterType, relatedEmployeeGroupFilterType)
VALUES (8, 0, 'КСП', 'KSP', '006', 'Д99', '06', 14, null, true, false, 'EXCLUDE', 'EXCLUDE');

INSERT INTO program_related_offices(programId, officeid)
VALUES (3, 2);
INSERT INTO program_related_offices(programId, officeid)
VALUES (3, 123);
INSERT INTO program_related_offices(programId, officeid)
VALUES (5, 2);
INSERT INTO program_related_offices(programId, officeid)
VALUES (5, 123);
INSERT INTO program_related_offices(programId, officeid)
VALUES (8, 2);

INSERT INTO program_related_group(programId, groupcode)
VALUES (3, 'РСХБ Страхование');
INSERT INTO program_related_group(programId, groupcode)
VALUES (5, 'АЛЬФА Страхование');


insert into audit_envers_info (id, timestamp)
VALUES (0, datediff('ms', '1970-01-01', now()));

-- Примеры тестовых стратегий программ страхования
INSERT INTO strategy(id, version, name, description, policycode, startdate, enddate, deleted, type)
VALUES (1, 0, 'Глобальные облигации', null, null, CURRENT_DATE, CURRENT_DATE, false, 'CLASSIC');
INSERT INTO strategy_AUD(id, name, description, policycode, startdate, enddate, deleted, REV, REVTYPE)
VALUES (1, 'Глобальные облигации', null, null, CURRENT_DATE, CURRENT_DATE, false, 0, 0);

INSERT INTO strategy(id, version, name, description, policycode, startdate, enddate, deleted, type)
VALUES (2, 0, 'Цифровое будущее', null, null, CURRENT_DATE, CURRENT_DATE, false, 'COUPON');
INSERT INTO strategy_AUD(id, name, description, policycode, startdate, enddate, deleted, REV, REVTYPE)
VALUES (2, 'Цифровое будущее', null, null, CURRENT_DATE, CURRENT_DATE, false, 0, 0);

INSERT INTO strategy(id, version, name, description, policycode, startdate, enddate, deleted, type)
VALUES (3, 0, 'Европейские лидеры', null, null, CURRENT_DATE, CURRENT_DATE, false, 'CLASSIC');
INSERT INTO strategy_AUD(id, name, description, policycode, startdate, enddate, deleted, REV, REVTYPE)
VALUES (3, 'Европейские лидеры', null, null, CURRENT_DATE, CURRENT_DATE, false, 0, 0);

INSERT INTO strategy_property(id, version, rate, strategy_id, ticker, base_index_source_strategy_id)
VALUES (1, 0, 1, 1, 'тикер 1', 1);
INSERT INTO strategy_property(id, version, rate, strategy_id, ticker, base_index_source_strategy_id)
VALUES (2, 0, 2, 1, 'тикер 2', 3);

INSERT INTO strategy_property(id, version, rate, strategy_id, base_index_source_strategy_id)
VALUES (3, 0, 1, 2, 2);
INSERT INTO strategy_property(id, version, rate, strategy_id, base_index_source_strategy_id)
VALUES (4, 0, 1, 3, 3);


-- Примеры тестовых параметров программ страхования
INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, insuranceamount, tariff,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, guarantee_level,
                            deleted)
VALUES (1, 0, (SELECT ID FROM program_v2 where name = 'Классический ИСЖ'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 1, 1, 100,
        100, 200, 'BY_FORMULA', 2.1, null, null, 100,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, 75.5, false);

insert into strategy_2_program_setting (strategyid, programsettingid)
VALUES (1, 1);
insert into strategy_2_program_setting (strategyid, programsettingid)
VALUES (2, 1);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender, insuranceamount,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (3, 0, (SELECT ID FROM program_v2 where name = 'Классический ИСЖ'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 2, 1, 100,
        100, 200, 'FIXED', 2.1, 100,
        'MONTHLY', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE', 300,
        120, 100, 100, 60, false);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (4, 0, (SELECT ID FROM program_v2 where name = 'Классический ИСЖ'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 2, null, null,
        null, null, 'FIXED', 2.1, null,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (5, 0, (SELECT ID FROM program_v2 where name = 'Классический ИСЖ'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 1, 1, 100,
        null, null, 'FIXED', 2.1, null,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, discount,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (2, 0, (SELECT id FROM program_v2 WHERE name = 'Купонный ИСЖ'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 1, 1, 100,
        100, 200, 'FIXED', 2.1, 10000, 10,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

insert into strategy_2_program_setting (strategyid, programsettingid)
VALUES (1, 2);
INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, insuranceamount, tariff,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (6, 0, (SELECT ID FROM program_v2 where name = 'НСЖ 2018'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 1, 1, 100,
        100, 200, 'BY_FORMULA', 2.1, null, null, 100,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, insuranceamount, tariff,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (7, 0, (SELECT ID FROM program_v2 where name = 'Упрощённый НСЖ'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 1, 1, 100,
        100, 200, 'BY_FORMULA', 2.1, null, null, 100,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, insuranceamount, tariff,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (8, 0, (SELECT ID FROM program_v2 where name = 'Рента'), CURRENT_DATE, CURRENT_DATE, true,
        7, 100, 'YEAR', 1, 1, 1000,
        1, 2000, 'BY_FORMULA', 2.1, null, null, 100,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, insuranceamount, tariff,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (9, 0, (SELECT ID FROM program_v2 where name = 'Коробка'), CURRENT_DATE, CURRENT_DATE, true,
        7, 100, 'YEAR', 1, 1, 1000,
        1, 2000, 'FIXED', 2.1, 50000, 50000, 100,
        'YEARLY', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);


INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, insuranceamount, tariff,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (10, 0, (SELECT ID FROM program_v2 where name = 'КСП'), CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 1, 1, 100,
        100, 200, 'BY_FORMULA', 2.1, null, null, 100,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, deleted)
VALUES (11, 0, (SELECT ID FROM program_v2 where name = 'Классический ИСЖ'), TIMESTAMP '2000-01-01',
        TIMESTAMP '2001-01-01', true,
        1, 100, 'YEAR', 1, 1, 100,
        null, null, 'FIXED', 2.1, null,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, false);

insert into required_document(id, version, type, active, deleted)
VALUES (1, 0, 'Документ, удостоверяющий личность.', true, false);
insert into required_document(id, version, type, active, deleted)
VALUES (2, 0, 'Платежное поручение/платежный документ.', true, false);
insert into required_document(id, version, type, active, deleted)
VALUES (3, 0, 'Форма самосертификации.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Медицинская анкета.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Справка о доходах.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Результаты проведённого медицинского обследования.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Заявление на страхование.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Договор страхования.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Уведомление о рисках.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Заявление о выплате.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Заявление на изменение договора.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Заявление об отказе.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Заявление о расторжении договора.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Иное.', true, false);
insert into required_document(version, type, active, deleted)
VALUES (0, 'Удаленный.', true, true);

insert into risk (id, version, program_kind, name, full_name, start_date, end_date, payment_method, benefits_insured,
                  deleted)
values (1, 0, 'ISJ', 'ЖИТИЕ МОЕ', 'ЖИТИЕ МОЕ', null, null, 'ONCE', TRUE, false);

insert into risk (id, version, program_kind, name, full_name, start_date, end_date, payment_method, benefits_insured,
                  deleted)
values (16, 0, 'ISJ', 'Дожитие', 'Дожитие', null, null, 'ONCE', TRUE, false);

insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (2, 0, 'ISJ', 'Смерть по любой причине', 'Смерть по любой причине', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (3, 0, 'ISJ', 'Смерть в результате несчастного случая', 'Смерть в результате несчастного случая', null, null,
        false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (4, 0, 'ISJ', 'Смерть в результате кораблекрушения / авиакатастрофы / крушения поезда',
        'Смерть в результате кораблекрушения / авиакатастрофы / крушения поезда', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (5, 0, 'NSJ', 'Дожитие', 'Дожитие', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (6, 0, 'NSJ', 'Смерть по любой причине', 'Смерть по любой причине', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (7, 0, 'NSJ', 'Смерть от несчастного случая', 'Смерть от несчастного случая', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (8, 0, 'NSJ', 'Инвалидность I группы', 'Инвалидность I группы', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (9, 0, 'NSJ', 'Травма', 'Травма', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (10, 0, 'NSJ', 'Госпитализация в результате НС', 'Госпитализация в результате НС', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (11, 0, 'NSJ', 'Временная нетрудоспособность в результате НС', 'Временная нетрудоспособность в результате НС',
        null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (12, 0, 'NSJ', 'Смерть по любой причине', 'Смерть по любой причине', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (13, 0, 'NSJ', 'Инвалидность I, II группы по любой причине.', 'Инвалидность I, II группы по любой причине.',
        null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (14, 0, 'NSJ', 'Инвалидность I, II, III группы НС.', 'Инвалидность I, II, III группы НС.', null, null, false);
insert into risk (id, version, program_kind, name, full_name, start_date, end_date, deleted)
values (15, 0, 'NSJ', 'Инвалидность I, II, III группы по любой причине',
        'Инвалидность I, II, III группы по любой причине', null, null, false);
insert into risk (id, version, program_kind, name, full_name, deleted)
values (17, 0, 'KSP', 'Дожитие', 'Дожитие', false);

INSERT INTO insurance_status(id, code, name)
VALUES (1, 'DRAFT', 'Котировка');
INSERT INTO insurance_status_AUD(id, code, name, REV, REVTYPE)
VALUES (1, 'DRAFT', 'Котировка', 0, 0);
INSERT INTO insurance_status(id, code, name)
VALUES (2, 'PROJECT', 'Проект');
INSERT INTO insurance_status_AUD(id, code, name, REV, REVTYPE)
VALUES (2, 'PROJECT', 'Проект', 0, 0);
INSERT INTO insurance_status(id, code, name)
VALUES (3, 'MADE', 'Оформлен');
INSERT INTO insurance_status_AUD(id, code, name, REV, REVTYPE)
VALUES (3, 'MADE', 'Оформлен', 0, 0);

INSERT INTO insurance_status(id, code, name)
VALUES (4, 'NEED_WITHDRAW_APPLICATION', 'Требуется заявление о выплате');
INSERT INTO insurance_status(id, code, name)
VALUES (5, 'WITHDRAW_APPLICATION_RECEIVED', 'Получено заявление о выплате');
INSERT INTO insurance_status(id, code, name)
VALUES (6, 'PAYMENT_FULFILLED', 'Выплата произведена');
INSERT INTO insurance_status(id, code, name)
VALUES (7, 'CHANGING_APPLICATION_RECEIVED', 'Получено заявление на изменение договора');
INSERT INTO insurance_status(id, code, name)
VALUES (8, 'REFUSING_APPLICATION_RECEIVED', 'Получено заявление об отказе');
INSERT INTO insurance_status(id, code, name)
VALUES (9, 'CANCELLATION_APPLICATION_RECEIVED', 'Получено заявление о расторжении договора');
INSERT INTO insurance_status(id, code, name)
VALUES (10, 'CANCELED', 'Расторгнут');
INSERT INTO insurance_status(id, code, name)
VALUES (11, 'REVOKED', 'Аннулирован');
INSERT INTO insurance_status(id, code, name)
VALUES (12, 'FINISHED', 'Окончен');
INSERT INTO insurance_status(id, code, name)
VALUES (13, 'MADE_NOT_COMPLETED', 'Оформление не завершено');
INSERT INTO insurance_status(id, code, name)
VALUES (14, 'REVOKED_REPLACEMENT', 'Анулирование по замене');
INSERT INTO insurance_status(id, code, name)
VALUES (15, 'PAYED', 'Оплачен');

-- Тестовые параметры рисков
INSERT INTO risk_setting(id, version, risktype, risk, signamount, minriskamount, maxriskamount,
                         calculationtype, riskamount, riskdependence, calculationcoefficient,
                         riskpremium, calculationcoefficientpremium,
                         parentrisk, riskreturnrate, otherriskparam, insurancerule, rulesdetails,
                         formula, recordamount, type, deleted)
VALUES (1, 0, 'REQUIRED', 6, false, null, null,
        'DEPENDS_ON_CONTRACT_SUM', null, null, 0.55,
        null, null,
        null, null, null, null, null,
        null, 'TOTAL_PREMIUM', 'CONSTANT', false);

INSERT INTO risk_setting(id, version, risktype, risk, signamount, minriskamount, maxriskamount,
                         calculationtype, riskamount, riskdependence, calculationcoefficient,
                         riskpremium, calculationcoefficientpremium,
                         parentrisk, riskreturnrate, otherriskparam, insurancerule, rulesdetails,
                         formula, recordamount, type, deleted)
VALUES (2, 0, 'REQUIRED', 5, false, null, null,
        'DEPENDS_ON_CONTRACT_PREMIUM', null, null, 2.3,
        null, null,
        null, null, null, null, null,
        null, 'TOTAL_PREMIUM', 'CONSTANT', false);

INSERT INTO risk_setting(id, version, risktype, risk, signamount, minriskamount, maxriskamount,
                         calculationtype, riskamount, riskdependence, calculationcoefficient,
                         riskpremium, calculationcoefficientpremium,
                         parentrisk, riskreturnrate, otherriskparam, insurancerule, rulesdetails,
                         formula, recordamount, type, deleted)
VALUES (3, 0, 'REQUIRED', 5, false, null, 10,
        'DEPENDS_ON_CONTRACT_PREMIUM', null, null, 200,
        null, null,
        null, null, null, null, null,
        null, 'TOTAL_PREMIUM', 'CONSTANT', false);

INSERT INTO risk_setting(id, version, risktype, risk, signamount, minriskamount, maxriskamount,
                         calculationtype, riskamount, riskdependence, calculationcoefficient,
                         riskpremium, calculationcoefficientpremium,
                         parentrisk, riskreturnrate, otherriskparam, insurancerule, rulesdetails,
                         formula, recordamount, type, deleted)
VALUES (4, 0, 'REQUIRED', 17, false, null, 10,
        'DEPENDS_ON_CONTRACT_PREMIUM', null, null, 200,
        null, null,
        null, null, null, null, null,
        null, 'TOTAL_PREMIUM', 'CONSTANT', false);

INSERT INTO risk_2_program_setting(riskid, programsettingid)
VALUES (4, 9);

insert into document_template_2_program_setting (documenttemplateid, programsettingid)
values ('RshbInsuranceRent01', 9);
insert into document_template_2_program_setting (documenttemplateid, programsettingid)
values ('RshbInsuranceRent02', 9);

insert into document_template_2_program_setting (documenttemplateid, programsettingid)
values ('RshbInsuranceRent01', 2);
insert into document_template_2_program_setting (documenttemplateid, programsettingid)
values ('RshbInsuranceRent02', 2);

INSERT INTO risk_2_program_setting(riskid, programsettingid)
VALUES (1, 8);

INSERT INTO risk_2_program_setting(riskid, programsettingid)
VALUES (1, 3);

INSERT INTO risk_2_program_setting(riskid, programsettingid)
VALUES (1, 1);
INSERT INTO risk_2_program_setting(riskid, programsettingid)
VALUES (2, 1);

INSERT INTO optional_risk_2_program_setting(riskid, programsettingid)
VALUES (1, 1);
INSERT INTO optional_risk_2_program_setting(riskid, programsettingid)
VALUES (2, 1);

INSERT INTO risk_2_program_setting(riskid, programsettingid)
VALUES (3, 7);

CREATE UNIQUE INDEX insurance_2_recipient_uniq ON insurance_2_recipient (insurance_id, surname, first_name, middle_name, birth_date);

CREATE UNIQUE INDEX insurance_2_risk_info_uniq ON insurance_2_risk_info (insurance_id, risk_id);


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
                       conclusiondate, strategy_id)
VALUES (2, TIMESTAMP '2018-12-12 12:12:12', '23К010110000001', 'Амурский РФ', '2310', 2, 9, 'Фёдоров Фёдор Фёдорович',
        2, 1, false, 1, 1, TRUE, 1, 3, TIMESTAMP '2018-11-27', 1, 1000, 1000, 10000, 10000, 'ONCE', 1, TRUE, 'YEAR', 10,
        TIMESTAMP '2018-12-12 12:12:12', 1);
insert into insurance (id, creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id,
                       employee_name, program_setting_id, version, deleted, holder_id, holder_version,
                       holder_equals_insured, insured_id, duration, start_date, currency, premium, rur_premium, amount,
                       rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm,
                       conclusiondate, strategy_id)
VALUES (3, TIMESTAMP '2018-12-13 13:13:13', '23К010112345678', 'Амурский РФ', '2310', 2, 8, 'Антонов Антон Антонович',
        1, 1, false, 2, 1, FALSE, 1, 3, TIMESTAMP '2018-12-04', 1, 1000, 1000, 10000, 10000, 'ONCE', 1, TRUE, 'YEAR',
        10, TIMESTAMP '2018-12-13 13:13:13', 2);
insert into insurance (id, creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id,
                       employee_name, program_setting_id, version, deleted, holder_id, holder_version,
                       holder_equals_insured, insured_id, duration, start_date, currency, premium, rur_premium, amount,
                       rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm,
                       conclusiondate)
VALUES (4, TIMESTAMP '2018-12-14 14:14:14', '23К010112345679', 'Амурский РФ', '2311', 3, 15,
        'Викторов Виктор Викторович', 1, 1, false, 3, 1, FALSE, 1, 3, TIMESTAMP '2018-12-29', 1, 1000, 1000, 10000,
        10000, 'ONCE', 1, TRUE, 'YEAR', 10, TIMESTAMP '2018-12-14 14:14:14');
insert into insurance (id, creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id,
                       employee_name, program_setting_id, version, deleted, holder_id, holder_version,
                       holder_equals_insured, insured_id, duration, start_date, currency, premium, rur_premium, amount,
                       rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm,
                       conclusiondate, full_set_document, strategy_id, guarantee_level, source)
VALUES (9, TIMESTAMP '2018-12-12 12:12:12', '23К010110000002', 'Амурский РФ', '2310', 2, 9, 'Фёдоров Фёдор Фёдорович',
        2, 1, false, 9, 1, TRUE, 9, 4, TIMESTAMP '2018-12-27', 1, 1000, 1000, 10000, 10000, 'YEARLY', 3, TRUE, 'YEAR',
        10,
        TIMESTAMP '2018-12-12 12:12:12', false, 2, 22, 'OFFICE');

insert into insurance (id, creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id,
                       employee_name, program_setting_id, version, deleted, holder_id, holder_version,
                       holder_equals_insured, insured_id, duration, start_date, currency, premium, rur_premium, amount,
                       rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm,
                       conclusiondate, full_set_document, source)
VALUES (10, TIMESTAMP '2018-12-15 15:15:15', '23К010110000003', 'Амурский РФ', '2310', 2, 9, 'Фёдоров Фёдор Фёдорович',
        2, 1, false, 9, 1, TRUE, 9, 4, TIMESTAMP '2018-12-28', 1, 1000, 1000, 10000, 10000, 'ONCE', 3, TRUE, 'YEAR', 10,
        TIMESTAMP '2018-12-15 15:15:15', true, 'INTERNET_CLIENT');


insert into insurance (id, creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id,
                       employee_name, program_setting_id, version, deleted, holder_id, holder_version,
                       holder_equals_insured, insured_id, duration, start_date, currency, premium, rur_premium, amount,
                       rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm,
                       conclusiondate, full_set_document)
VALUES (11, TIMESTAMP '2010-12-15 15:15:15', '23К010110009123', 'Амурский РФ', '2310', 2, 9, 'Фёдоров Фёдор Фёдорович',
        9, 1, false, 9, 1, TRUE, 9, 4, TIMESTAMP '2010-12-28', 1, 1000, 1000, 10000, 10000, 'ONCE', 13, TRUE, 'YEAR',
        10,
        TIMESTAMP '2010-12-15 15:15:15', false);

-- Примеры тестовых договоров страхования
insert into insurance (id, creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id,
                       employee_name, program_setting_id, version, deleted, holder_id, holder_version,
                       holder_equals_insured, insured_id, duration, start_date, currency, premium, rur_premium, amount,
                       rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm,
                       conclusiondate, strategy_id)
VALUES (12, TIMESTAMP '2018-12-12 12:12:12', '23К010110086434', 'Амурский РФ', '2310', 2, 9, 'Фёдоров Фёдор Фёдорович',
        2, 1, false, 1, 1, TRUE, 1, 3, TIMESTAMP '2018-11-27', 1, 1000, 1000, 10000, 10000, 'ONCE', 15, TRUE, 'YEAR', 10,
        TIMESTAMP '2018-12-12 12:12:12', 1);

ALTER TABLE attachments
    DROP COLUMN IF EXISTS content;

ALTER TABLE attachments
    ADD COLUMN content BYTEA;

ALTER TABLE images
    DROP COLUMN IF EXISTS image;

ALTER TABLE images
    ADD COLUMN image BYTEA;

ALTER TABLE extract
    DROP COLUMN IF EXISTS content;

ALTER TABLE extract
    ADD COLUMN content BYTEA;

insert into extract(uuid, request_digest, name, content, status, type)
values ('d1bd76e2-5762-4b6a-adee-5c5c46826240', 'fd85e710644641578466c25d9bb5f9e7',
        'Отчёт по продажам ISJ с 2016-07-08 по 2018-12-20.xlsx', rawtohex('1'), 'CREATING', 'SALE_REPORT');

INSERT INTO attachments(id, kind, documenttype, createdate, contract, filename, comment,
                        owner, ownername, verified, deleted, content)
VALUES (1, 'INSURANCE_CONTRACT', 1, TIMESTAMP '2018-01-01 12:12:12', 2, 'test1.xml', null,
        1, 'Петров Иван Сидорович', false, false, rawtohex('1'));

INSERT INTO attachments(id, kind, documenttype, createdate, contract, filename, comment,
                        owner, ownername, verified, deleted, content)
VALUES (2, 'INSURANCE_CONTRACT', 1, TIMESTAMP '2018-01-02 12:12:13', 2, 'test2.xml', null,
        1, 'Петров Иван Сидорович', false, false, rawtohex('1'));

INSERT INTO attachments(id, kind, documenttype, createdate, contract, filename, comment,
                        owner, ownername, verified, deleted, content)
VALUES (3, 'INSURANCE_CONTRACT', 1, TIMESTAMP '2018-01-02 12:12:14', 2, 'test3.xml', null,
        1, 'Петров Иван Сидорович', false, false, rawtohex('1'));

INSERT INTO attachments(id, kind, documenttype, createdate, contract, filename, comment,
                        owner, ownername, verified, deleted, content)
VALUES (4, 'INSURANCE_CONTRACT', 1, TIMESTAMP '2018-01-02 12:12:15', 2, 'test4.xml', null,
        1, 'Петров Иван Сидорович', false, false, rawtohex('1'));

INSERT INTO attachments(id, kind, documenttype, createdate, contract, filename, comment,
                        owner, ownername, verified, deleted, content)
VALUES (5, 'INSURANCE_CONTRACT', 3, TIMESTAMP '2018-01-02 12:12:16', 2, 'test2.xml', null,
        1, 'Петров Иван Сидорович', false, false, rawtohex('1'));



INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (1, 1, 'FIELD_SET', 'documents', 'Документы клиента', null, false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (2, 1, 'FIELD', 'riskLevel', 'Уровень риска', null, false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (3, 1, 'FIELD', 'docSeries', 'Серия документа', (SELECT id FROM required_field WHERE strid = 'documents'),
        false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (4, 1, 'FIELD', 'docNumber', 'Номер документа', (SELECT id FROM required_field WHERE strid = 'documents'),
        false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (5, 1, 'FIELD', 'issuedBy', 'Кем выдан документ', (SELECT id FROM required_field WHERE strid = 'documents'),
        false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (6, 1, 'FIELD', 'issuedDate', 'Когда выдан документ', (SELECT id FROM required_field WHERE strid = 'documents'),
        false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (7, 1, 'FIELD', 'isActive', 'Признак активности', (SELECT id FROM required_field WHERE strid = 'documents'),
        false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (8, 1, 'FIELD', 'isMain', 'Признак основного документа',
        (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (9, 1, 'FIELD', 'divisionCode', 'Код подразделения', (SELECT id FROM required_field WHERE strid = 'documents'),
        false);
INSERT INTO required_field(id, version, type, strid, name, parent, deleted)
VALUES (10, 1, 'FIELD', 'issuedEndDate', 'Дата окончания действия документа',
        (SELECT id FROM required_field WHERE strid = 'documents'), false);

INSERT INTO required_field_2_program_setting(requiredfieldid, programsettingid)
VALUES (1, 3);

INSERT INTO required_document_setting(id, version, requireddocument, status, deleted)
VALUES (1, 1, 2, 'DRAFT', false);

INSERT INTO required_document_setting(id, version, requireddocument, status, deleted)
VALUES (2, 1, 3, 'MADE', false);

INSERT INTO required_document_setting_2_program_setting(requireddocumentsettingid, programsettingid)
VALUES (1, 3);
INSERT INTO required_document_setting_2_program_setting(requireddocumentsettingid, programsettingid)
VALUES (2, 3);


CREATE UNIQUE INDEX redemption_uniq ON redemption (program_id, currency_id, duration, periodicity);

CREATE UNIQUE INDEX redemption_coefficient_uniq ON redemption_coefficient (redemption_id, period);

INSERT INTO redemption (id, version, program_id, currency_id, duration, periodicity, payment_period, deleted)
VALUES (3, 0, (SELECT ID FROM program_v2 where name = 'Купонный ИСЖ'), 1, 2, 'YEARLY', 'YEARLY', false);

INSERT INTO redemption_coefficient (id, period, coefficient, redemption_id)
VALUES (1, 1, 1, 3);
INSERT INTO redemption_coefficient (id, period, coefficient, redemption_id)
VALUES (2, 2, 2, 3);
INSERT INTO redemption_coefficient (id, period, coefficient, redemption_id)
VALUES (3, 3, 1, 3);

INSERT INTO redemption (id, version, program_id, currency_id, duration, periodicity, payment_period, deleted)
VALUES (4, 0, (SELECT ID FROM program_v2 where name = 'Купонный ИСЖ'), 1, 4, 'YEARLY', 'YEARLY', false);

INSERT INTO redemption_coefficient (id, period, coefficient, redemption_id)
VALUES (4, 1, 1, 4);
INSERT INTO redemption_coefficient (id, period, coefficient, redemption_id)
VALUES (5, 2, 2, 4);
INSERT INTO redemption_coefficient (id, period, coefficient, redemption_id)
VALUES (6, 3, 1, 4);


INSERT INTO insurance_2_recipient(id, insurance_id, surname, first_name, middle_name, birth_date,
                                  birth_place, tax_residence, relationship, share, birth_country)
VALUES (1, 2, 'ОченьДлиннаяФамилия1', 'ОченьДлинноеИмя1', 'ОченьДлинноеОтчество', TIMESTAMP '1980-01-01 12:12:12',
        'г. Севастополь', 'RUSSIAN', null, 25, 'RUSSIA');
INSERT INTO insurance_2_recipient(id, insurance_id, surname, first_name, middle_name, birth_date,
                                  birth_place, tax_residence, relationship, share, birth_country)
VALUES (2, 2, 'ЕщёДлиннееОченьДлиннаяФамилия2', 'ОченьДлинноеИмя2', 'ОченьДлинноеОтчество',
        TIMESTAMP '1980-01-01 12:12:12',
        'г. Симферополь', 'RUSSIAN', null, 25, 'RUSSIA');
INSERT INTO insurance_2_recipient(id, insurance_id, surname, first_name, middle_name, birth_date,
                                  birth_place, tax_residence, relationship, share, birth_country)
VALUES (3, 2, 'Иванов', 'Иван', null, TIMESTAMP '1970-01-01 12:12:12',
        'г. Москва', 'RUSSIAN', null, 25, 'RUSSIA');
INSERT INTO insurance_2_recipient(id, insurance_id, surname, first_name, middle_name, birth_date,
                                  birth_place, tax_residence, relationship, share, birth_country)
VALUES (4, 2, 'Шамсутдинов', 'Ильгизар', 'Ильгизович оглы', TIMESTAMP '1955-01-01 12:12:12',
        'г. Казань', 'RUSSIAN', null, 5, 'RUSSIA');
INSERT INTO insurance_2_recipient(id, insurance_id, surname, first_name, middle_name, birth_date,
                                  birth_place, tax_residence, relationship, share, birth_country)
VALUES (5, 2, 'Дружинин', 'Николай', 'Петрович', TIMESTAMP '2000-01-01 12:12:12',
        'г. Краснодар', 'RUSSIAN', null, 20, 'RUSSIA');

INSERT INTO insurance_2_risk_info(id, risk_id, insurance_id, amount, underwriting_rate, premium)
VALUES (1, 1, 2, 100, null, 200);


INSERT INTO insurance_2_risk_info(id, risk_id, insurance_id, amount, underwriting_rate, premium)
VALUES (2, 16, 2, 100, null, 200);

CREATE UNIQUE INDEX IF NOT EXISTS insurance_unique_contract_number
    ON insurance (contract_number);


INSERT INTO contract_number_sequence (lastId, programKind)
SELECT CASE p2.type
           WHEN 'KSP' THEN 6000
           ELSE COUNT(ins.id)
           END,
       p2.type
FROM insurance ins
         INNER JOIN program_setting ps ON ps.id = ins.program_setting_id
         INNER JOIN program_v2 p2 ON p2.id = ps.program
GROUP BY p2.type;

CREATE
ALIAS TO_NUMBER AS '
Long toNumber(String value, String format) {
    return 5L;
}
';

-- Заполнение тестовыми данными таблицы истории перехода по статусам договоров
INSERT INTO insurance_status_history(id, insurance_id, status_id, change_date, employee_id, employee_name,
                                     subdivision_id, subdivision_name, description)
VALUES (1, 2, 1, TIMESTAMP '2018-01-01 12:12:12', 1, 'Иванов Иван Иванович',
        1, 'Какое-то подразделение', 'Какое-то описание');
INSERT INTO insurance_status_history(id, insurance_id, status_id, change_date, employee_id, employee_name,
                                     subdivision_id, subdivision_name, description)
VALUES (2, 3, 2, TIMESTAMP '2018-01-01 14:11:12', 2, 'Петров Пётр Петрович',
        1, 'Какое-то подразделение', 'Какое-то описание');
INSERT INTO insurance_status_history(id, insurance_id, status_id, change_date, employee_id, employee_name,
                                     subdivision_id, subdivision_name, description)
VALUES (3, 4, 3, TIMESTAMP '2018-01-01 15:32:12', 1, 'Иванов Иван Иванович',
        1, 'Какое-то подразделение', 'Какое-то описание');
INSERT INTO insurance_status_history(id, insurance_id, status_id, change_date, employee_id, employee_name,
                                     subdivision_id, subdivision_name, description)
VALUES (4, 2, 4, TIMESTAMP '2018-01-01 17:44:12', 1, 'Иванов Иван Иванович',
        1, 'Какое-то подразделение', 'Какое-то описание');
INSERT INTO insurance_status_history(id, insurance_id, status_id, change_date, employee_id, employee_name,
                                     subdivision_id, subdivision_name, description)
VALUES (5, 2, 5, TIMESTAMP '2018-01-02 07:12:12', 1, 'Иванов Иван Иванович',
        1, 'Какое-то подразделение', 'Какое-то описание');


DROP TABLE IF EXISTS change_report;
DROP VIEW IF EXISTS change_report;
-- Создание вьюхи с отчётом по изенениям
CREATE VIEW change_report AS
SELECT rownum() AS id,
       t.contract_number,
       t.conclusion_date,
       t.holder_name,
       t.change_date,
       t.prev_status,
       t.next_status,
       t.attached_doc_type,
       t.full_set,
       t.employee_name
FROM (SELECT nextstatus.id                                                                 AS status_id,
             ins.contract_number                                                           AS contract_number,
             ins.conclusiondate                                                            AS conclusion_date,
             (((cl.surname || ' ') || cl.firstname)) || COALESCE(' ' || cl.middlename, '') AS holder_name,
             nextstatus.change_date                                                        AS change_date,
             COALESCE((SELECT prevstatusname.name
                       FROM insurance_status_history prev
                                LEFT JOIN insurance_status prevstatusname ON prev.status_id = prevstatusname.id
                       WHERE prev.insurance_id = nextstatus.insurance_id
                         AND prev.id < nextstatus.id
                       ORDER BY prev.id DESC
                       LIMIT 1), 'Нет статуса')                                            AS prev_status,
             statusname.name                                                               AS next_status,
             NULL                                                                          AS attached_doc_type,
             COALESCE(ins.full_set_document, false)                                        AS full_set,
             nextstatus.employee_name                                                      AS employee_name
      FROM insurance ins
               LEFT JOIN clients cl ON cl.id = ins.holder_id
               LEFT JOIN insurance_status_history nextstatus ON nextstatus.insurance_id = ins.id
               LEFT JOIN insurance_status statusname ON nextstatus.status_id = statusname.id
      UNION ALL
      SELECT NULL                                                                          AS status_id,
             ins.contract_number                                                           AS contract_number,
             ins.conclusiondate                                                            AS conclusion_date,
             (((cl.surname || ' ') || cl.firstname)) || COALESCE(' ' || cl.middlename, '') AS holder_name,
             attach.createdate                                                             AS change_date,
             COALESCE((SELECT prevstatusname.name
                       FROM insurance_status_history prev
                                LEFT JOIN insurance_status prevstatusname ON prev.status_id = prevstatusname.id
                       WHERE prev.insurance_id = nextstatus.insurance_id
                         AND prev.id < nextstatus.id
                       ORDER BY prev.id DESC
                       LIMIT 1), 'Нет статуса')                                            AS prev_status,
             COALESCE(statusname.name, 'Нет статуса')                                      AS next_status,
             docname.type                                                                  AS attached_doc_type,
             COALESCE(ins.full_set_document, false)                                        AS full_set,
             attach.ownername                                                              AS employee_name
      FROM attachments attach
               JOIN required_document docname ON attach.documenttype = docname.id
               LEFT JOIN insurance ins ON attach.contract = ins.id
               LEFT JOIN insurance_status_history nextstatus ON nextstatus.insurance_id = ins.id AND nextstatus.id =
                                                                                                     (SELECT MAX(id)
                                                                                                      FROM insurance_status_history st
                                                                                                      WHERE st.change_date < attach.createdate
                                                                                                        AND st.insurance_id = ins.id)

               LEFT JOIN insurance_status statusname ON nextstatus.status_id = statusname.id
               LEFT JOIN clients cl ON cl.id = ins.holder_id) t
ORDER BY t.contract_number, t.change_date;


--
INSERT INTO orders(id, creation_date, contract_id, client_id, amount, currency_iso_code,
                   ext_id, redirect_url, order_code, error_code, error_message,
                   pan, expiration, card_holder_name, ip, modified_date)
VALUES (1, TIMESTAMP '2019-02-19 12:26:11', 11, null, 1234, 810,
        null, null, null, null, null,
        null, null, null, null, TIMESTAMP '2019-02-21 12:26:11');

INSERT INTO orders(id, creation_date, contract_id, client_id, amount, currency_iso_code,
                   ext_id, redirect_url, order_code, error_code, error_message,
                   pan, expiration, card_holder_name, ip, modified_date)
VALUES (2, TIMESTAMP '2019-02-20 12:26:11', 11, 2, 1234, 810,
        null, null, 2, null, null,
        null, null, null, null, TIMESTAMP '2019-02-21 12:26:11');

INSERT INTO orders(id, creation_date, contract_id, client_id, amount, currency_iso_code,
                   ext_id, redirect_url, order_code, error_code, error_message,
                   pan, expiration, card_holder_name, ip, modified_date)
VALUES (3, TIMESTAMP '2019-02-21 12:26:11', null, null, 1234, 810,
        null, null, null, null, null,
        null, null, null, null, TIMESTAMP '2019-02-21 12:26:11');


insert into share (id, name)
values (1, 'FUTURE1');
insert into share (id, name)
values (2, 'FUTURE2');
insert into quote (share_id, date, value)
values (1, TIMESTAMP '2018-12-18', 10);
insert into quote (share_id, date, value)
values (1, TIMESTAMP '2019-02-10', 10);
insert into quote (share_id, date, value)
values (2, TIMESTAMP '2019-02-10', 10);
insert into quote (share_id, date, value)
values (1, TIMESTAMP '2019-02-11', 11);
insert into quote (share_id, date, value)
values (1, TIMESTAMP '2019-02-12', 12);
insert into share_2_strategy (share_id, strategy_id)
values (1, 2);
insert into share_2_strategy (share_id, strategy_id)
values (2, 2);

alter table base_index
    add UNIQUE (date, strategy_name);
insert into base_index (strategy_name, date, value)
values ('Глобальные облигации', TIMESTAMP '2018-12-17', 1368.16);
insert into base_index (strategy_name, date, value)
values ('Глобальные облигации', TIMESTAMP '2018-12-18', 1399.7);
insert into base_index (strategy_name, date, value)
values ('Глобальные облигации', TIMESTAMP '2018-12-27', 1367.61);
insert into base_index (strategy_name, date, value)
values ('Глобальные облигации', TIMESTAMP '2018-12-28', 1368);
insert into base_index (strategy_name, date, value)
values ('Глобальные облигации', TIMESTAMP '2018-12-29', 1399.7);

insert into base_index_2_strategy (base_index_date, strategy_name, strategy_id)
values (TIMESTAMP '2018-12-17', 'Глобальные облигации', 1);
insert into base_index_2_strategy (base_index_date, strategy_name, strategy_id)
values (TIMESTAMP '2018-12-18', 'Глобальные облигации', 1);
insert into base_index_2_strategy (base_index_date, strategy_name, strategy_id)
values (TIMESTAMP '2018-12-27', 'Глобальные облигации', 1);
insert into base_index_2_strategy (base_index_date, strategy_name, strategy_id)
values (TIMESTAMP '2018-12-28', 'Глобальные облигации', 1);
insert into base_index_2_strategy (base_index_date, strategy_name, strategy_id)
values (TIMESTAMP '2018-12-29', 'Глобальные облигации', 1);


INSERT INTO topic_request(id, name, description, email, is_active)
VALUES (0, 'Расторжение договора', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active)
VALUES (1, 'Страховой случай', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active)
VALUES (2, 'Изменение персональных данных', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active)
VALUES (3, 'Изменение в договоре страхования', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active)
VALUES (4, 'Покупка страхового продукта', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active)
VALUES (5, 'Заявление на покупку страхового продукта', '', 'testuser@mailforspam.com', false);

INSERT INTO request(id, request_date, request_topic, program_id, insurance_id, status, client_id, phone, email,
                    request_text, is_active, additional_info)
VALUES (0, TIMESTAMP '2019-04-08', 0, null, null, 'PROCESSED', 9, '+79123456789', 'ivanov@example.org',
        'Хочу расторгнуть договор', true, null);

insert into images (id, image, deleted)
values (1, rawtohex('1'), false);
insert into images (id, image, name, deleted)
values (2, rawtohex('1'), 'dbImage.png', false);

insert into acquiring_program (id, kind, program_setting_id, start_date, end_date, title, authorized_enable,
                               not_authorized_enable, application, description, link, image_id, priority)
values (1, 'KSP', 9, TIMESTAMP '2018-12-18', TIMESTAMP '2035-12-18', 'Наименование 1', true, true, false, 'описание 1',
        'http://rshbins.ru', 1, 2);
insert into acquiring_program (id, kind, program_setting_id, start_date, end_date, title, authorized_enable,
                               not_authorized_enable, application, description, link, image_id, priority)
values (2, 'KSP', 9, TIMESTAMP '2018-12-18', TIMESTAMP '2035-12-18', 'Наименование 2', false, true, true, 'описание 2',
        'http://rshbins.ru', 1, 1);
insert into acquiring_program (id, kind, program_setting_id, start_date, end_date, title, authorized_enable,
                               not_authorized_enable, application, description, link, image_id, priority)
values (3, 'KSP', 9, TIMESTAMP '2018-12-18', TIMESTAMP '2035-12-18', 'Наименование 3', true, false, false, 'описание 3',
        'http://rshbins.ru', 1, 2);
insert into acquiring_program (id, kind, program_setting_id, start_date, end_date, title, authorized_enable,
                               not_authorized_enable, application, description, link, image_id, priority)
values (4, 'KSP', 9, TIMESTAMP '2018-12-18', TIMESTAMP '2019-01-10', 'Наименование 4', false, true, false, 'описание 4',
        'http://rshbins.ru', 1, 2);
insert into acquiring_program (id, kind, program_setting_id, start_date, end_date, title, authorized_enable,
                               not_authorized_enable, application, description, link, image_id, priority)
values (5, 'KSP', 9, TIMESTAMP '2088-12-18', TIMESTAMP '2100-01-10', 'Наименование 5', false, true, false, 'описание 5',
        'http://rshbins.ru', 1, 2);
insert into acquiring_info (uuid, creation_date, acquiring_program_id, client_id, surname, firstname, middlename,
                            birth_date, gender, doc_number, doc_series, phone, email, status, insurance_id)
values ('111', TIMESTAMP '2088-12-18', 2, 1, 'Фамилия', 'Имя', 'Отчество', TIMESTAMP '1980-01-01', 'MALE', '111111',
        '1111', '+791100000083', 'email@email.ru', 'CREATED', 11);
insert into acquiring_info (uuid, creation_date, acquiring_program_id, client_id, surname, firstname, middlename,
                            birth_date, gender, doc_number, doc_series, phone, email, status)
values ('222', TIMESTAMP '2088-12-18', 2, 1, 'Фамилия', 'Имя', 'Отчество', TIMESTAMP '1980-01-01', 'MALE', '111111',
        '1111', '+791100000083', 'email@email.ru', 'REGISTRATION');

insert into document_template_2_acquiring_program (documenttemplateid, programId)
values ('RshbInsuranceRent01', 2);


INSERT INTO audit_envers_info(id, timestamp, user_id)
values (1, 1553067396581, 13);
INSERT INTO audit_envers_info(id, timestamp, user_id)
values (3, 1553067789793, 13);
INSERT INTO audit_envers_info(id, timestamp, user_id)
values (7, 1553068936217, 13);
INSERT INTO audit_envers_info(id, timestamp, user_id)
values (9, 1553156546732, 13);
INSERT INTO audit_envers_info(id, timestamp, user_id)
values (10, 1553157858985, 13);

INSERT INTO clients_aud(id, surname, firstname, middlename, birth_date, gender, birth_place,
                        resident, resident_rf, tax_residence, tax_payer_number, public_official_status,
                        foreign_public_official_type, russian_public_official_type, relations,
                        public_official_position, public_official_name_and_position,
                        beneficial_owner, business_relations, activities_goal, business_relations_goal,
                        risk_level_desc, business_reputation, financial_stability, finances_source,
                        personal_data_consent, inn, snils, license_driver, sign_ipdl,
                        sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner,
                        has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level,
                        latin_name, code_word, status, email,
                        registration_date, cache_save_time, last_update_date, rev, revtype)
VALUES (1, 'Кристиан', 'Бейл', 'Младший', TIMESTAMP '1986-03-30', 'MALE', 'СССР',
        'russian', true, 'RUSSIAN', '1234567891234', 'NONE',
        null, null, null,
        'Директор', 'Иванов Иван Иванович, Директор',
        null, null, null, null,
        null, null, 'STABLE', 'Бизнес',
        true, '123456789123', '12345678910', null, false,
        false, false, false, false,
        false, 'NOT_BANKRUPT', '', 'NORMAL',
        'IVANOV IVAN IVANOVICH', 'someword', 'STATUS-OK', 'ivanov@example.org',
        TIMESTAMP '2018-01-01', CURRENT_DATE, TIMESTAMP '2018-01-01', 7, 1);

INSERT INTO clients_aud(id, surname, firstname, middlename, birth_date, gender, birth_place,
                        resident, resident_rf, tax_residence, tax_payer_number, public_official_status,
                        foreign_public_official_type, russian_public_official_type, relations,
                        public_official_position, public_official_name_and_position,
                        beneficial_owner, business_relations, activities_goal, business_relations_goal,
                        risk_level_desc, business_reputation, financial_stability, finances_source,
                        personal_data_consent, inn, snils, license_driver, sign_ipdl,
                        sign_pdl, adoption_pdl_written_decision, has_fatca, has_beneficial_owner,
                        has_beneficiary, bankruptcy_info, bankruptcy_stage, risk_level,
                        latin_name, code_word, status, email,
                        registration_date, cache_save_time, last_update_date, rev, revtype)
VALUES (1, 'Крис', 'Хемсворт', 'Старший', TIMESTAMP '1986-03-30', 'MALE', 'СССР',
        'russian', true, 'RUSSIAN', '1234567891234', 'NONE',
        null, null, null,
        'Директор', 'Иванов Иван Иванович, Директор',
        null, null, null, null,
        null, null, 'STABLE', 'Бизнес',
        true, '123456789123', '12345678910', null, false,
        false, false, false, false,
        false, 'NOT_BANKRUPT', '', 'NORMAL',
        'IVANOV IVAN IVANOVICH', 'someword', 'STATUS-OK', 'ivanov@example.org',
        TIMESTAMP '2018-01-01', CURRENT_DATE, TIMESTAMP '2018-01-01', 3, 0);

INSERT INTO addresses_for_client_aud(id, address_type, country, region, area, city, locality, street,
                                     house, construction, housing, apartment, index, registration_period_start,
                                     registration_period_end, client_id, rev, revtype)
VALUES (1, 'REGISTRATION', 'RUSSIA', null, 'Вашингтон', null, null, null,
        null, null, null, null, null, null,
        null, 1, 9, 1);
INSERT INTO addresses_for_client_aud(id, address_type, country, region, area, city, locality, street,
                                     house, construction, housing, apartment, index, registration_period_start,
                                     registration_period_end, client_id, rev, revtype)
VALUES (1, 'REGISTRATION', 'RUSSIA', null, 'Пало-альто', null, null, null,
        null, null, null, null, null, null,
        null, 1, 1, 0);

INSERT INTO documents_for_client_aud(id, doc_type, doc_series, doc_number, issued_by, issued_date,
                                     is_active, is_main, division_code, issued_end_date, is_valid_document,
                                     stay_start_date, stay_end_date, client_id, scan_id, is_identity,
                                     doc_name, rev, revtype)
VALUES (1, 'PASSPORT_RF', '9999', '123456', null, null,
        true, true, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 1, null, true,
        'паспорт', 10, 1);

INSERT INTO documents_for_client_aud(id, doc_type, doc_series, doc_number, issued_by, issued_date,
                                     is_active, is_main, division_code, issued_end_date, is_valid_document,
                                     stay_start_date, stay_end_date, client_id, scan_id, is_identity,
                                     doc_name, rev, revtype)
VALUES (1, 'PASSPORT_RF', '9999', '123456', null, null,
        true, true, null, null, true,
        CURRENT_DATE, TIMESTAMP '2020-01-01', 1, null, true,
        'паспорт', 3, 1);

INSERT INTO phones_for_client_aud(id, phone_type, number, main, verified, notification, client_id, rev, revtype)
VALUES (1, 'MOBILE', '+791100000001', true, true, true, 1, 7, 0);

INSERT INTO phones_for_client_aud(id, phone_type, number, main, verified, notification, client_id, rev, revtype)
VALUES (1, 'MOBILE', '+139110000000', true, true, true, 1, 10, 1);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (1, 'ISJ', true);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (2, 'NSJ', false);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (3, 'KSP', false);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (4, 'RENT', false);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (5, 'HOME', true);

INSERT INTO user_template (id, name, priority, templateid, filename)
VALUES (1, 'Пример документа', 2, 'test-template', 'test.jrxml');

INSERT INTO user_template (id, name, priority, templateid, filename)
VALUES (2, 'Инструкция', 3, 'test-template', 'test.jrxml');

INSERT INTO user_template (id, name, priority, templateid, filename)
VALUES (3, 'Очень важный документ, который отображается первым', 1, 'test-template', 'test.jrxml');

insert into client_template (id, kind, program_id, is_template, name, description, link, start_date, end_date,
                             attach_id, sort_priority)
values (1, 'ISJ', null, false, 'Без программы', 'Описание 1', 'http://google.com', TIMESTAMP '2019-01-01',
        TIMESTAMP '2020-01-01', null, 1);
insert into client_template (id, kind, program_id, is_template, name, description, link, start_date, end_date,
                             attach_id, sort_priority)
values (2, 'NSJ', null, true, 'Шаблон 2', 'Описание 2', null, TIMESTAMP '2019-01-02', TIMESTAMP '2020-01-02', 1, 2);
insert into client_template (id, kind, program_id, is_template, name, description, link, start_date, end_date,
                             attach_id, sort_priority)
values (5, 'ISJ', 2, false, 'С видом и программой 1', 'Описание 3', null, TIMESTAMP '2019-01-05',
        TIMESTAMP '2020-01-05', 2, 5);
insert into client_template (id, kind, program_id, is_template, name, description, link, start_date, end_date,
                             attach_id, sort_priority)
values (3, 'ISJ', 2, false, 'С видом и программой 2', 'Описание 3', null, TIMESTAMP '2019-01-03',
        TIMESTAMP '2020-01-03', 2, 3);
insert into client_template (id, kind, program_id, is_template, name, description, link, start_date, end_date,
                             attach_id, sort_priority)
values (4, null, null, false, 'Без вида', 'Описание 4', 'http://google.com', TIMESTAMP '2019-01-04',
        TIMESTAMP '2020-01-04', null, 4);

INSERT INTO insurance_status(code, name)
VALUES ('PAYED', 'Оплачен');
INSERT INTO insurance_status(code, name)
VALUES ('CRM_IMPORTED', 'Выгружен в CRM');
