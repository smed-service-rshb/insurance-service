set search_path to 'insurance_service';

CREATE TABLE user_template (
    id                   BIGSERIAL NOT NULL,
    name                 VARCHAR(150)  NOT NULL,
    priority             INTEGER DEFAULT 1,
    templateId           VARCHAR(36),
    PRIMARY KEY (id)
);

COMMENT ON TABLE user_template                        IS 'Справочник пользовательских шаблонов документов';
COMMENT ON COLUMN user_template.name                  IS 'Наименование, которое будет отображаться пользователю. Текстовое поле длиной 150 символов';
COMMENT ON COLUMN user_template.priority              IS 'Приоритет. Задаётся от 1 до бесконечности. Чем меньше число, тем выше приоритет';
COMMENT ON COLUMN user_template.templateId            IS 'ID записи шаблона документа в таблице print-templates (микросервисе common-dict).';