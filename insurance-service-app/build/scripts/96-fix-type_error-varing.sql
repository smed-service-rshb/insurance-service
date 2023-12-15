set search_path to 'insurance_service';

alter table insurance_service.extract
alter column type type varchar(60) using type::varchar(60);
