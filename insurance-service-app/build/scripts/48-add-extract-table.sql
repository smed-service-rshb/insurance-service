set search_path to 'insurance_service';

CREATE TABLE extract (
  uuid            VARCHAR(40) NOT NULL,
  request_digest  VARCHAR(32) NOT NULL,
  name            VARCHAR(255),
  creation_date   TIMESTAMP,
  content         BYTEA,
  status          VARCHAR(20),
  type            VARCHAR(30),
  CONSTRAINT extract_pkey PRIMARY KEY (uuid)
);