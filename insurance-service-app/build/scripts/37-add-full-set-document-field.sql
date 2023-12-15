set search_path to 'insurance_service';
--признак получения поного комплекта документов
ALTER TABLE insurance ADD column if not exists full_set_document boolean DEFAULT FALSE NULL;