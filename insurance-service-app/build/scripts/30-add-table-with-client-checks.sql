set search_path to 'insurance_service';

CREATE TABLE client_check (
  id BIGSERIAL NOT NULL,
  check_type CHARACTER VARYING(50) NOT NULL,
  check_result CHARACTER VARYING(50) NOT NULL,
  client_id BIGINT,
  comment CHARACTER VARYING(50),
  creation_date     TIMESTAMP,
  CONSTRAINT client_check_client_fkey FOREIGN KEY (client_id) REFERENCES clients (id),
  CONSTRAINT client_check_pkey PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS client_check_client_id ON client_check (client_id);

