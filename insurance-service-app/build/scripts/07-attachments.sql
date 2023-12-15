set search_path to 'insurance_service';

--Таблица вложений, сканов документов
CREATE TABLE attachments (
  id              VARCHAR(36)  NOT NULL,
  kind            VARCHAR(100)  NOT NULL,
  documentType    BIGINT,
  createDate      TIMESTAMP DEFAULT current_timestamp,
  contract        BIGINT,
  fileName        VARCHAR(254),
  comment         VARCHAR(254),
  owner           BIGINT,
  verified        BOOLEAN,
  content         BYTEA,
  deleted         BOOLEAN,
  CONSTRAINT PK_ATTACHMENTS PRIMARY KEY (id)
);

ALTER TABLE attachments
  ADD CONSTRAINT attachments_documenttype_fk
FOREIGN KEY (documentType)
REFERENCES required_document (id);


ALTER TABLE attachments
  ADD CONSTRAINT attachments_contract_fk
FOREIGN KEY (contract)
REFERENCES insurance (id);
