set search_path to 'insurance_service';

CREATE TABLE topic_request (
   id                BIGSERIAL         NOT NULL,
   name              VARCHAR(51)       NOT NULL,
   description       VARCHAR(564),
   email             VARCHAR(256)      NOT NULL,
   is_active         BOOLEAN           NOT NULL,
   PRIMARY KEY (ID)
);

CREATE TABLE request (
   id                BIGSERIAL      NOT NULL,
   request_date      TIMESTAMP      NOT NULL,
   request_topic     BIGSERIAL      NOT NULL       references topic_request (id),
   program_id        BIGSERIAL                     references program_v2 (id),
   insurance_id      BIGSERIAL                     references insurance (id),
   status            varchar(30)    NOT NULL,
   client_id         BIGSERIAL      NOT NULL       references clients (id),
   phone             VARCHAR(256),
   email             VARCHAR(256),
   request_text      VARCHAR(1001),
   is_active         BOOLEAN,
   additional_info   VARCHAR(1001),
   PRIMARY KEY (ID)
);

CREATE TABLE request_attachments (
   id                BIGSERIAL NOT NULL,
   attachment_id     VARCHAR(36)                   references attachments (id),
   report_id         BIGSERIAL                     references request (id),
   is_deleted        BOOLEAN,
   PRIMARY KEY (ID)
);


INSERT INTO topic_request(id, name, description, email, is_active) VALUES (0, 'Расторжение договора', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active) VALUES (1, 'Страховой случай', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active) VALUES (2, 'Изменение персональных данных', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active) VALUES (3, 'Изменение в договоре страхования', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active) VALUES (4, 'Покупка страхового продукта', '', 'testuser@mailforspam.com', true);
INSERT INTO topic_request(id, name, description, email, is_active) VALUES (5, 'Заявление на покупку страхового продукта', '', 'testuser@mailforspam.com', false);