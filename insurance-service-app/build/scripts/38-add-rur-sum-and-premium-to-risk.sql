set search_path to 'insurance_service';
--премия по риску в национальной валюте
ALTER TABLE insurance_2_risk_info ADD column if not exists rur_premium NUMERIC;
--сумма по риску в национальной валюте
ALTER TABLE insurance_2_risk_info ADD column if not exists rur_amount NUMERIC;
