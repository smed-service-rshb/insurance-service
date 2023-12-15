set search_path to 'insurance_service';

alter table request_attachments
rename column report_id to request_id;

