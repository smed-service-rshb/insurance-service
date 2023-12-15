-- Создание пользователя и предоставление прав
DROP USER IF EXISTS insurance_service;
CREATE USER insurance_service WITH PASSWORD 'insurance_service';
GRANT ALL ON SCHEMA insurance_service TO insurance_service;
ALTER DEFAULT PRIVILEGES IN SCHEMA insurance_service GRANT ALL ON TABLES TO insurance_service;
ALTER DEFAULT PRIVILEGES IN SCHEMA insurance_service GRANT ALL ON SEQUENCES TO insurance_service;

set search_path to 'insurance_service';

CREATE TABLE contract (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  creation_date     TIMESTAMP,
  contract_number   VARCHAR(255),
  surname           VARCHAR(255) NOT NULL,
  first_name        VARCHAR(255) NOT NULL,
  middle_name       VARCHAR(255),
  birth_date        TIMESTAMP,
  email             VARCHAR(255),
  phone_number      VARCHAR(255),
  employee_id       BIGINT NOT NULL,
  employee_personnel_number VARCHAR(20) NOT NULL,
  employee_name     VARCHAR(255) NOT NULL,
  branch_name       VARCHAR(255),
  subdivision_name  VARCHAR(255),
  program           BIGINT NOT NULL,
  subdivision_id    BIGINT NOT NULL,
  deleted           BOOLEAN,

  PRIMARY KEY (ID)
);

CREATE TABLE required_document (
  id           BIGSERIAL NOT NULL,
  version      BIGINT NOT NULL,
  type         VARCHAR(50) NOT NULL,
  active       BOOLEAN,
  deleted      BOOLEAN,

  PRIMARY KEY (ID)
);

CREATE TABLE rights_to_permission (
	right_id	    VARCHAR(60)  NOT NULL,
	permission_id	VARCHAR(255) NOT NULL,
	PRIMARY KEY(right_id, permission_id)
);

CREATE TABLE program (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  name              VARCHAR(255) NOT NULL,
  code              VARCHAR(255) NOT NULL,
  minAge            INT NULL,
  maxAge            INT NULL,

  PRIMARY KEY (ID)
);

create table risk (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  program_kind      VARCHAR(3) NOT NULL,
  name              VARCHAR(100) NOT NULL,
  full_name         VARCHAR(150) NOT NULL,
  start_date        TIMESTAMP,
  end_date          TIMESTAMP,
  benefits_insured   BOOLEAN,
  deleted           BOOLEAN,

  PRIMARY KEY (ID)
);

CREATE OR REPLACE FUNCTION setup_permission_rights(i_permission VARCHAR(255), i_rights varchar[])
	RETURNS void AS $$
DECLARE
	rightItem varchar;
BEGIN
	FOREACH rightItem IN ARRAY i_rights LOOP
		INSERT INTO rights_to_permission(right_id, permission_id) VALUES (rightItem, i_permission);
	END LOOP;
END;
$$ LANGUAGE plpgsql;