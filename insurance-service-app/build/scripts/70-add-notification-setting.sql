set search_path to 'insurance_service';

ALTER TABLE insurance_service.settings ALTER COLUMN key TYPE varchar(40) USING key::varchar(40);

insert into settings (key, value, description)
values ('clientDuplicateNotificationEmail', '', 'email для уведомлений о возможных дублях клиентов');