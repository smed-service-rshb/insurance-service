set search_path to 'insurance_service';

-- Связанные задачи
-- 130112: РСХБ-СЖ_ФО. Авторизация. Администратору предоставить полные права
-- 130424: РСХБ-СЖ_ФО. Авторизация. Ограничение доступа по ВСП не везде учитывается

-- Актуализация имени поля name таблицы program_related_offices в соответствии с его функциональным назначением
-- Теперь вместо наименования ВСП будет хранится его ID из auth-service
ALTER TABLE program_related_offices RENAME name  TO officeid;

-- Конвертация данных с name на id, используя закономерность для текущего состояния таблиц
-- insurance_service.program_related_offices и auth_service.orgunit - формирование ID
-- для РФ = константа '100' + номер;
-- для ВСП = константа '200' + номер ВСП;
UPDATE program_related_offices
SET officeid = ('200' || LPAD(officeid::text, 4, '0'))::BIGINT;

-- Смена типа данных с STRING на LONG с преобразованием
ALTER TABLE program_related_offices ALTER COLUMN officeid TYPE bigint USING (officeid::bigint);

-- Актуализация имени поля name таблицы program_related_group в соответствии с его функциональным назначением
ALTER TABLE program_related_group RENAME name  TO groupcode;
