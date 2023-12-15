set search_path to 'insurance_service';
ALTER TABLE client_check ALTER COLUMN check_result TYPE character varying(254);
ALTER TABLE client_check ALTER COLUMN comment TYPE character varying(1000);
