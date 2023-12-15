set search_path to 'insurance_service';

-- Добавление информации о файле справочнике в запись с результатом проверки по клиенту
ALTER TABLE insurance_service.client_check ADD COLUMN IF NOT EXISTS update_id bigint;
