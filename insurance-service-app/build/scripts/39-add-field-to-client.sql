set search_path to 'insurance_service';
--Код страны по классификатору ОКСМО
ALTER TABLE clients ADD column if not exists country_code  varchar(3) NULL;
--Регион
ALTER TABLE clients ADD column if not exists birth_region varchar(255) NULL;
--Код ОКАТО региона
ALTER TABLE clients ADD column if not exists birth_region_okato varchar(11) NULL;
--Район рождения
ALTER TABLE clients ADD column if not exists birth_area varchar(255) NULL;
--Код страны по классификатору ОКСМО
ALTER TABLE addresses_for_client ADD column if not exists country_code varchar(3) NULL;
--Код ОКАТО региона
ALTER TABLE addresses_for_client ADD column if not exists okato varchar(11) NULL;