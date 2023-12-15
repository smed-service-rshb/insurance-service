set search_path to 'insurance_service';

-- Исправление ошибки при выгрузке анкеты клиента в XML
UPDATE addresses_for_client SET country = 'Российская Федерация' WHERE country = 'Россия' or country = 'RUSSIA';
