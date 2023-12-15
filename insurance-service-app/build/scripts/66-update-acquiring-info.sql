set search_path to 'insurance_service';

ALTER TABLE insurance_service.acquiring_info ALTER COLUMN doc_number DROP NOT NULL;
ALTER TABLE insurance_service.acquiring_info ALTER COLUMN doc_series DROP NOT NULL;
ALTER TABLE insurance_service.acquiring_info ALTER COLUMN email DROP NOT NULL;