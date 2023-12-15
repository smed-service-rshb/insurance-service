set search_path to 'insurance_service';


--1-base-line-script.sql
--клиенты
CREATE TABLE clients (
  id BIGSERIAL NOT NULL,
  surname CHARACTER VARYING(150),
  firstname CHARACTER VARYING(150),
  middlename CHARACTER VARYING(150),
  birth_date DATE,
  gender CHARACTER VARYING(6),
  birth_place CHARACTER VARYING(254),
  birth_country CHARACTER VARYING(100),
  resident CHARACTER VARYING(10),
  resident_rf BOOLEAN,
  tax_residence CHARACTER VARYING(10),
  tax_payer_number CHARACTER VARYING(15),
  public_official_status CHARACTER VARYING(15),
  foreign_public_official_type CHARACTER VARYING(15),
  russian_public_official_type CHARACTER VARYING(15),
  relations CHARACTER VARYING(15),
  public_official_position CHARACTER VARYING(200),
  public_official_name_and_position CHARACTER VARYING(250),
  beneficial_owner CHARACTER VARYING(200),
  business_relations CHARACTER VARYING(50),
  activities_goal CHARACTER VARYING(50),
  business_relations_goal CHARACTER VARYING(50),
  risk_level_desc CHARACTER VARYING(100),
  business_reputation CHARACTER VARYING(50),
  financial_stability CHARACTER VARYING(50),
  finances_source CHARACTER VARYING(50),
  personal_data_consent BOOLEAN,
  inn CHARACTER VARYING(12),
  snils CHARACTER VARYING(11),
  license_driver CHARACTER VARYING(10),
  sign_ipdl BOOLEAN,
  sign_pdl BOOLEAN,
  adoption_pdl_written_decision BOOLEAN,
  has_fatca BOOLEAN,
  has_beneficial_owner BOOLEAN,
  has_beneficiary BOOLEAN,
  bankruptcy_info CHARACTER VARYING(100),
  bankruptcy_stage CHARACTER VARYING(20),
  risk_level CHARACTER VARYING(100),
  latin_name CHARACTER VARYING(80),
  code_word CHARACTER VARYING(20),
  status CHARACTER VARYING(20),
  email CHARACTER VARYING(50),
  agreements CHARACTER VARYING(1500),
  nationalities CHARACTER VARYING(200),
  registration_date DATE,
  cache_save_time TIMESTAMP,
  last_update_date TIMESTAMP,
  citizenship_country CHARACTER VARYING(100),
  CONSTRAINT create_clients_pkey PRIMARY KEY (id)
);

CREATE TABLE person_decisions (
  id BIGSERIAL NOT NULL,
  check_unit_type CHARACTER VARYING(50) NOT NULL,
  decision CHARACTER VARYING(50),
  client_id BIGINT,
  comment CHARACTER VARYING(50),
  CONSTRAINT person_decisions_fkey FOREIGN KEY (client_id) REFERENCES clients (id),
  CONSTRAINT person_decisions_pkey PRIMARY KEY (id),
  UNIQUE(check_unit_type, client_id)
);

--документы клиентов
CREATE TABLE documents_for_client (
  id BIGSERIAL NOT NULL,
  doc_type CHARACTER VARYING(40) NOT NULL,
  doc_series CHARACTER VARYING(10),
  doc_number CHARACTER VARYING(20),
  issued_by CHARACTER VARYING(100),
  issued_date DATE,
  is_active BOOLEAN,
  is_main BOOLEAN NOT NULL,
  division_code CHARACTER VARYING(6),
  issued_end_date DATE,
  is_valid_document BOOLEAN,
  stay_start_date DATE,
  stay_end_date DATE,
  client_id BIGINT NOT NULL,
  scan_id CHARACTER VARYING(150),
  is_identity BOOLEAN,
  doc_name CHARACTER VARYING(100),
  CONSTRAINT documents_for_client_pkey PRIMARY KEY (id),
  CONSTRAINT documents_for_client_fkey FOREIGN KEY (client_id) REFERENCES clients (id),
  UNIQUE(doc_type, doc_number, doc_series, client_id)
);

--телефоны клиентов
CREATE TABLE phones_for_client (
  id BIGSERIAL NOT NULL,
  phone_type CHARACTER VARYING(20) NOT NULL,
  number CHARACTER VARYING(20) NOT NULL,
  main BOOLEAN,
  verified BOOLEAN,
  notification BOOLEAN,
  client_id BIGINT NOT NULL,
  CONSTRAINT phones_for_client_pkey PRIMARY KEY (id),
  CONSTRAINT phones_for_client_fkey FOREIGN KEY (client_id) REFERENCES clients (id),
  UNIQUE(number, client_id)
);

--адреса клиентов
CREATE TABLE addresses_for_client (
  id BIGSERIAL NOT NULL,
  address_type CHARACTER VARYING(12) NOT NULL,
  country CHARACTER VARYING(50),
  region CHARACTER VARYING(150),
  area CHARACTER VARYING(150),
  city CHARACTER VARYING(150),
  locality CHARACTER VARYING(100),
  street CHARACTER VARYING(150),
  house CHARACTER VARYING(10),
  construction CHARACTER VARYING(10),
  housing CHARACTER VARYING(10),
  apartment CHARACTER VARYING(10),
  index CHARACTER VARYING(6),
  registration_period_start DATE,
  registration_period_end DATE,
  client_id BIGINT NOT NULL,
  CONSTRAINT addresses_for_client_pkey PRIMARY KEY (id),
  CONSTRAINT addresses_for_client_fkey FOREIGN KEY (client_id) REFERENCES clients (id)
);

CREATE TABLE client_resources (
  id           BIGSERIAL    NOT NULL,
  client_id    VARCHAR(60)  NOT NULL,
  system_id    VARCHAR(100) NOT NULL,
  resource_key VARCHAR(200) NOT NULL,
  resource_id  VARCHAR(20)  NOT NULL,
  file_name VARCHAR(50),
  PRIMARY KEY (id),
  UNIQUE (system_id, client_id, resource_id)
);

--Настройки схемы прав
CREATE TABLE permission_links (
  id          BIGSERIAL NOT NULL,
  permission  VARCHAR(255),
  role        VARCHAR(60),
  PRIMARY KEY (id)
);

CREATE TABLE settings (
  id          BIGSERIAL    NOT NULL,
  key         VARCHAR(30)  NOT NULL UNIQUE,
  value       VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  CONSTRAINT PK_SETTINGS PRIMARY KEY (ID)
);

CREATE TABLE client_employees (
  id               BIGSERIAL    NOT NULL,
  surname          VARCHAR(100) NOT NULL,
  middlename       VARCHAR(100),
  firstname        VARCHAR(100) NOT NULL,
  personnel_number VARCHAR(100) NOT NULL,
  office           VARCHAR(100) NOT NULL,
  position         VARCHAR(100),
  client           BIGINT       NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT employees_for_client_fkey FOREIGN KEY (client) REFERENCES clients (id)
);


--2-clients-data-service-permissions.sql
SELECT setup_permission_rights('access-clients-info', ARRAY ['ACCESS_CLIENTS_EXCEPT_MAIN_OFFICE', 'ACCESS_CLIENTS_MAIN_OFFICE']);
SELECT setup_permission_rights('create-or-edit-clients', ARRAY ['EXECUTE_CREATE_CLIENT', 'EXECUTE_EDIT_CLIENT']);
SELECT setup_permission_rights('inspect-edit-clients', ARRAY ['INSPECT_EDIT_CLIENT']);
SELECT setup_permission_rights('access-clients-info', ARRAY ['EXECUTE_FUNDS_MOVEMENT_STATEMENT_PROCESS']);

--Увеличиваем размер колонок в истории
--ALTER TABLE act_hi_varinst ALTER COLUMN text_ TYPE CHARACTER VARYING(10000);
--ALTER TABLE act_hi_varinst ALTER COLUMN text2_ TYPE CHARACTER VARYING(10000);
--ALTER TABLE act_ru_variable ALTER COLUMN text_ TYPE CHARACTER VARYING(10000);
--ALTER TABLE act_ru_variable ALTER COLUMN text2_ TYPE CHARACTER VARYING(10000);