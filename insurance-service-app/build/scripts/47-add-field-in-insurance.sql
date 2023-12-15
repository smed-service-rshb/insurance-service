set search_path to 'insurance_service';

alter table insurance_service.insurance
  add column if not exists comment_for_not_full_documents varchar(255);