set search_path to 'insurance_service';

-- Связь между шаблоном документов и параметрами программы страхования
CREATE TABLE document_template_2_acquiring_program (
  documentTemplateId  VARCHAR(100) NOT NULL,
  programId    BIGINT NOT NULL,
  PRIMARY KEY (documentTemplateId, programId)
);

ALTER TABLE document_template_2_acquiring_program
  ADD CONSTRAINT doc_templ_acquiringprogram_fk
FOREIGN KEY (programId)
REFERENCES acquiring_program (id);

ALTER TABLE insurance ADD column if not exists acquiring_program_id bigint NULL;

ALTER TABLE insurance ADD CONSTRAINT insurance_acquiring_program_fk
FOREIGN KEY (acquiring_program_id)
REFERENCES acquiring_program (id);