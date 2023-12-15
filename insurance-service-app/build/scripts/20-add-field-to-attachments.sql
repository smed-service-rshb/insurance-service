set search_path to 'insurance_service';

ALTER TABLE attachments ADD contract_uuid varchar(40) NULL;
ALTER TABLE insurance ADD uuid varchar(40) NULL;