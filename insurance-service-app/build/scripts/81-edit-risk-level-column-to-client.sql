set search_path to 'insurance_service';

-- увеличение размера поля
ALTER TABLE clients alter column risk_level_desc type varchar(500) using risk_level_desc::varchar(500);
ALTER TABLE clients_aud alter column risk_level_desc type varchar(500) using risk_level_desc::varchar(500);