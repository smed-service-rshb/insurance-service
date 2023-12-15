set search_path to 'insurance_service';

--Таблица справочника выкупных коэффициентов
CREATE TABLE redemption (
  id          BIGSERIAL NOT NULL,
  version     BIGINT    NOT NULL,
  program_id  BIGINT    NOT NULL,
  currency_id BIGINT    NOT NULL,
  duration    INTEGER,
  periodicity VARCHAR(50),
  deleted     BOOLEAN default FALSE,

  CONSTRAINT PK_REDEMPTION PRIMARY KEY (id),
  CONSTRAINT redemption_program_fkey FOREIGN KEY (program_id) REFERENCES program_v2 (id)
);

CREATE UNIQUE INDEX redemption_unique_constraint ON redemption (program_id, currency_id, duration, periodicity)
   WHERE redemption.deleted is not true;

--Таблица выкупных коэффициентов
CREATE TABLE redemption_coefficient (
  id          BIGSERIAL NOT NULL,
  period      INTEGER   NOT NULL,
  redemption_id BIGINT    NOT NULL,
  coefficient NUMERIC   NOT NULL,

   CONSTRAINT PK_REDEMPTION_COEFF PRIMARY KEY (id),
   UNIQUE (period, redemption_id)
)
