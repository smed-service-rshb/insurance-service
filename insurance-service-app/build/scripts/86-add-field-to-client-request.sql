set search_path to 'insurance_service';
SELECT setup_permission_rights('update-client-request-attachment', ARRAY ['CLIENT_REQUEST_CREATE', 'CLIENT_REQUEST_PROCESSING']);

ALTER TABLE insurance_service.request ADD column if not exists comment VARCHAR(1000);