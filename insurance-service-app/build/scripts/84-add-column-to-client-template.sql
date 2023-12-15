set search_path to 'insurance_service';

-- приоритет отображения в списке щаблонов
ALTER TABLE client_template ADD COLUMN IF NOT EXISTS sort_priority bigint;


