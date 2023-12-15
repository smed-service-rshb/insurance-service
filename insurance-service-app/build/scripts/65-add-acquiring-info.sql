set search_path to 'insurance_service';


CREATE TABLE acquiring_info (
  uuid                 VARCHAR(40)  NOT NULL,
  creation_date        TIMESTAMP,
  acquiring_program_id BIGINT       NOT NULL,
  client_id            BIGINT,
  surname              VARCHAR(100) NOT NULL,
  firstname            VARCHAR(100) NOT NULL,
  middlename           VARCHAR(100),
  birth_date           DATE         NOT NULL,
  gender               VARCHAR(15)  NOT NULL,
  doc_number           VARCHAR(6)   NOT NULL,
  doc_series           VARCHAR(4)   NOT NULL,
  phone                VARCHAR(11)  NOT NULL,
  email                VARCHAR(255) NOT NULL,
  address              VARCHAR(255),
  insurance_id         BIGINT,
  status               VARCHAR(25)  NOT NULL,
  description          VARCHAR(255),

  PRIMARY KEY (uuid),
  CONSTRAINT acquiring_info_program_id_fkey FOREIGN KEY (acquiring_program_id) REFERENCES acquiring_program (id),
  CONSTRAINT acquiring_info_insurance_id_fkey FOREIGN KEY (insurance_id) REFERENCES insurance (id)
);

insert into settings (key, value, description)
values ('systemEmployeeId', '', 'Идентификатор системного работника');