set search_path to 'insurance_service';

CREATE TABLE images (
   id                BIGSERIAL         NOT NULL,
   image             BYTEA,
   name              VARCHAR(255),
   deleted           BOOLEAN           NOT NULL,
   PRIMARY KEY (ID)
);


CREATE TABLE acquiring_program (
   id                    BIGSERIAL       NOT NULL,
   kind                  VARCHAR(10),
   program_setting_id    BIGINT          NOT NULL,
   start_date            DATE            NOT NULL,
   end_date              DATE,
   title                 VARCHAR(100)    NOT NULL,
   authorized_enable     BOOLEAN,
   not_authorized_enable BOOLEAN,
   application           BOOLEAN,
   description           VARCHAR(1000),
   link                  VARCHAR(255),
   image_id              BIGINT          NOT NULL,
   priority              INT,

   PRIMARY KEY (ID),
   CONSTRAINT program_setting_id_fkey FOREIGN KEY (program_setting_id) REFERENCES program_setting (id),
   CONSTRAINT image_id_fkey FOREIGN KEY (image_id) REFERENCES images (id)
);