set search_path to 'insurance_service';

CREATE TABLE programKind_avail (
  id                   BIGSERIAL NOT NULL,
  programKind          VARCHAR(50)  NOT NULL,
  isActive             BOOLEAN,
  PRIMARY KEY (id)
);

COMMENT ON TABLE programKind_avail                  IS 'Справочник доступности видов программ';
COMMENT ON COLUMN programKind_avail.programKind     IS 'Код вида программы, например ISJ, NSJ, KSP, RENT, HOME';
COMMENT ON COLUMN programKind_avail.isActive        IS 'Признак активности данного вида программы';

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (1, 'ISJ', true);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (2, 'NSJ', true);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (3, 'KSP', true);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (4, 'RENT', true);

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (5, 'HOME', false);