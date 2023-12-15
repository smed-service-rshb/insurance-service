set search_path to 'insurance_service';

insert into settings (key, value, description)
values ('documentDataChecks', 'false', 'Параметр включения проверки по документам при создании договора')
ON CONFLICT DO NOTHING;