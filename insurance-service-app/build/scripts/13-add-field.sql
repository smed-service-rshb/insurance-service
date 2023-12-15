set search_path to 'insurance_service';

ALTER TABLE insurance_2_risk_info ADD column if not exists otherriskparam varchar(200) NULL;
ALTER TABLE program_setting ADD column if not exists paymentTerm int NULL;
ALTER TABLE insurance ADD column if not exists paymentTerm int NULL;