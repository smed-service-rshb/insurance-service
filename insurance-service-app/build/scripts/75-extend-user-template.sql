set search_path to 'insurance_service';

ALTER TABLE user_template
    ADD COLUMN fileName VARCHAR(300);

COMMENT ON COLUMN user_template.fileName IS 'Наименование файла, которое увидит пользователь при скачивании';
