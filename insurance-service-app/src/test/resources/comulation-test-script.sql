INSERT INTO program_v2(id, version, name,name_for_print, type, number, policycode, variant, coolingperiod, segment, isactive, deleted,
                       relatedOfficeFilterType, relatedEmployeeGroupFilterType, comulation)
VALUES (200, 0, 'Купонный ИСЖ', 'Купонный ИСЖ', 'ISJ', '002', 'Ю99', '01', 14, null, true, false, 'INCLUDE_ALL', 'INCLUDE_ALL', 1000);

INSERT INTO program_setting(id, version, program, startdate, enddate, policyholderinsured,
                            minimumterm, maximumterm, calendarunit, currency, minsum, maxsum,
                            minpremium, maxpremium, premiummethod, coefficient, bonusamount, insuranceamount, tariff,
                            periodicity, underwriting, minageholder, maxageholder, minageinsured,
                            maxageinsured, mingrowth, maxgrowth, minweight, maxweight, gender,
                            maxupperpressure, minupperpressure, maxlowerpressure, minlowerpressure, guarantee_level,
                            deleted)
VALUES (12, 0, 200, CURRENT_DATE, CURRENT_DATE, true,
        1, 100, 'YEAR', 1, 1, 100,
        100, 200, 'BY_FORMULA', 2.1, null, null, 100,
        'ONCE', 'NO_STATEMENT', 18, 54, 18,
        54, 100, 220, 45, 220, 'MALE',
        120, 100, 100, 60, 75.5, false);

insert into insurance (id, creation_date, contract_number, branch_name, subdivision_name, subdivision_Id, employee_id,
                       employee_name, program_setting_id, version, deleted, holder_id, holder_version,
                       holder_equals_insured, insured_id, duration, start_date, currency, premium, rur_premium, amount,
                       rur_amount, periodicity, status_id, recipient_equals_holder, calendar_unit, paymentterm,
                       conclusiondate, strategy_id)
VALUES (28, TIMESTAMP '2018-12-12 12:12:12', '23К010110001001', 'Амурский РФ', '2310', 2, 9, 'Фёдоров Фёдор Фёдорович',
        12, 1, false, 1, 1, TRUE, 1, 3, TIMESTAMP '2018-11-27', 1, 600, 600, 600, 600, 'ONCE', 15, TRUE, 'YEAR', 10,
        TIMESTAMP '2018-12-12 12:12:12', 1);