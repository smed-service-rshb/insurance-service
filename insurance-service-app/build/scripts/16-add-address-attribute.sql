set search_path to 'insurance_service';
-- Добавление поля адрес фактического проживания совпадает с адресом регистрации
ALTER TABLE insurance_service.clients ADD column if not exists equals_addresses boolean NULL;
