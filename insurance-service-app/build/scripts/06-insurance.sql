set search_path to 'insurance_service';

-- Статусы договора страхования
CREATE TABLE insurance_status (
  id            BIGSERIAL NOT NULL,
  code          VARCHAR(50) NOT NULL,
  name          VARCHAR(150) NOT NULL,
  CONSTRAINT insurance_status_pkey PRIMARY KEY (id),
  UNIQUE (code)
);

CREATE TABLE insurance (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  creation_date     TIMESTAMP,
  contract_number  VARCHAR(255),
  employee_id       BIGINT NOT NULL,
  employee_name       VARCHAR(100),
  program_setting_id  BIGINT NOT NULL,
  subdivision_id    BIGINT NOT NULL,
  subdivision_name    VARCHAR(255),
  branch_id    BIGINT,
  branch_name    VARCHAR(255),
  deleted           BOOLEAN,
  holder_id BIGINT,
  holder_version INTEGER,
  insured_id BIGINT,
  insured_version INTEGER,
  holder_equals_insured BOOLEAN DEFAULT TRUE,
  duration INTEGER,
  start_date DATE,
  close_date DATE,
  currency BIGINT,
  exchange_rate NUMERIC,
  premium NUMERIC,
  rur_premium NUMERIC,
  amount NUMERIC,
  rur_amount NUMERIC,
  periodicity VARCHAR(50),
  growth INTEGER,
  weight INTEGER,
  upper_pressure INTEGER,
  lower_pressure INTEGER,
  guarantee_level NUMERIC,
  participation_rate NUMERIC,
  status_id BIGINT,
  source VARCHAR(50),
  recipient_equals_holder BOOLEAN,
  strategy_id BIGINT,
  calendar_unit VARCHAR(50) NOT NULL,
  is_calc_by_sum BOOLEAN,
  CONSTRAINT insurance_program_param_fkey FOREIGN KEY (program_setting_id) REFERENCES program_setting (id),
  CONSTRAINT insurance_status_fkey FOREIGN KEY (status_id) REFERENCES insurance_status (id),
  CONSTRAINT insurance_strategy_fkey FOREIGN KEY (strategy_id) REFERENCES strategy (id),
  CONSTRAINT insurance_pkey PRIMARY  KEY (ID)
);

comment on column insurance.holder_id is 'Страхователь';
comment on column insurance.holder_version is 'Версия анкеты страхователя';
comment on column insurance.insured_id is 'Застрахованное лицо';
comment on column insurance.insured_version is 'Версия анкеты застрахованного лица';
comment on column insurance.holder_equals_insured is 'Признак, что Страхователь является Застрахованным';
comment on column insurance.duration is 'Срок страхования';
comment on column insurance.start_date is 'Дата начала действия договора страхования';
comment on column insurance.close_date is 'Дата закрытия договора страхования';
comment on column insurance.currency is 'Валюта договора';
comment on column insurance.exchange_rate is 'Курс валюты договора к рублю РФ на дату оформления договора';
comment on column insurance.premium is 'Страховая премия в валюте договора';
comment on column insurance.rur_premium is 'Страховая премия в национальной валюте';
comment on column insurance.amount is 'Страховая сумма в валюте договора';
comment on column insurance.rur_amount is 'Страховая сумма в национальной валюте';
comment on column insurance.periodicity is 'Периодичность уплаты взносов';
comment on column insurance.growth is 'Рост застрахованного. Указывается в сантиметрах.';
comment on column insurance.weight is 'Вес застрахованного. Указывается в килограммах';
comment on column insurance.upper_pressure is 'Давление верхнее застрахованного';
comment on column insurance.lower_pressure is 'Давление нижнее застрахованного';
comment on column insurance.guarantee_level is 'ИСЖ.	Уровень гарантии (%)';
comment on column insurance.participation_rate is 'ИСЖ.	Коэффициент участия (%)';
comment on column insurance.status_id is 'Статус';
comment on column insurance.strategy_id is 'Идентификатор стратегии';
comment on column insurance.calendar_unit is 'единица срока страхования';

/**
Информация по основным рискам по договору
 */
CREATE TABLE insurance_2_risk_info (
  id                  BIGSERIAL NOT NULL,
  risk_id             BIGINT,
  insurance_id         BIGINT,
  amount              NUMERIC,
  underwriting_rate   NUMERIC,
  premium             NUMERIC,
  CONSTRAINT insurance_2_risk_info_pkey PRIMARY KEY (id),
  CONSTRAINT insurance_2_risk_info_insurance_fkey FOREIGN KEY (insurance_id) REFERENCES insurance (id),
  CONSTRAINT insurance_2_risk_info_risk_fkey FOREIGN KEY (risk_id) REFERENCES risk (id),
  UNIQUE (insurance_id, risk_id)
);

/**
Информация по дополнительным рискам по договору
 */
CREATE TABLE insurance_2_add_risk_info (
  id                  BIGSERIAL NOT NULL,
  risk_id             BIGINT,
  insurance_id         BIGINT,
  amount              NUMERIC,
  premium             NUMERIC,
  CONSTRAINT insurance_2_add_risk_info_pkey PRIMARY KEY (id),
  CONSTRAINT insurance_2_add_risk_info_insurance_fkey FOREIGN KEY (insurance_id) REFERENCES insurance (id),
  CONSTRAINT insurance_2_add_risk_info_risk_fkey FOREIGN KEY (risk_id) REFERENCES risk (id),
  UNIQUE (insurance_id, risk_id)
);

--Выгодоприобретатели
CREATE TABLE insurance_2_recipient (
  id                  BIGSERIAL NOT NULL,
  insurance_id        BIGINT,
  surname             VARCHAR(255) NOT NULL,
  first_name          VARCHAR(255) NOT NULL,
  middle_name         VARCHAR(255),
  birth_date          DATE,
  birth_place         VARCHAR(255),
  tax_residence       VARCHAR(50),
  relationship        VARCHAR(50),
  share               NUMERIC,
  birth_country       VARCHAR(100),
  CONSTRAINT insurance_2_recipient_pkey PRIMARY KEY (id),
  UNIQUE (insurance_id, surname, first_name, middle_name, birth_date)
);

--Доли выгодоприобретателей в рисках по договору
CREATE TABLE insurance_2_recipient_2_risk (
  id                  BIGSERIAL NOT NULL,
  recipient_id        BIGINT,
  risk_id             BIGINT,
  percent             NUMERIC,
  CONSTRAINT insurance_2_recipient_2_risk_pkey PRIMARY KEY (id),
  CONSTRAINT insurance_2_recipient_2_risk_recipient_fkey FOREIGN KEY (recipient_id) REFERENCES insurance_2_recipient (id),
  CONSTRAINT insurance_2_recipient_2_risk_risk_fkey FOREIGN KEY (risk_id) REFERENCES risk (id),
  UNIQUE (recipient_id, risk_id)
);

--Выкупные суммы по договору
CREATE TABLE insurance_2_buyout (
  id                  BIGSERIAL NOT NULL,
  insurance_id        BIGINT,
  period              INTEGER,
  rate                NUMERIC,
  CONSTRAINT insurance_2_buyout_pkey PRIMARY KEY (id),
  CONSTRAINT insurance_2_buyout_insurance_fkey FOREIGN KEY (insurance_id) REFERENCES insurance (id),
  UNIQUE (insurance_id, period)
);


-- История изменения статусов по документу
CREATE TABLE insurance_status_history (
  id                  BIGSERIAL NOT NULL,
  insurance_id        BIGINT NOT NULL,
  status_id           BIGINT NOT NULL,
  change_date         DATE NOT NULL DEFAULT current_date,
  employee_id         BIGINT,
  employee_name       VARCHAR(100),
  subdivision_id       BIGINT,
  subdivision_name     VARCHAR(255),
  description         VARCHAR(255),
  CONSTRAINT insurance_status_history_pkey PRIMARY KEY (id),
  CONSTRAINT insurance_status_history_insurance_fkey FOREIGN KEY (insurance_id) REFERENCES insurance (id),
  CONSTRAINT insurance_status_history_status_fkey FOREIGN KEY (status_id) REFERENCES insurance_status (id)
);

INSERT INTO insurance_status(id, code, name) VALUES (1, 'DRAFT', 'Котировка');
INSERT INTO insurance_status(id, code, name) VALUES (2, 'PROJECT', 'Проект');
INSERT INTO insurance_status(id, code, name) VALUES (3, 'MADE', 'Оформлен');
INSERT INTO insurance_status(id, code, name) VALUES (4, 'NEED_WITHDRAW_APPLICATION', 'Требуется заявление о выплате');
INSERT INTO insurance_status(id, code, name) VALUES (5, 'WITHDRAW_APPLICATION_RECEIVED', 'Получено заявление о выплате');
INSERT INTO insurance_status(id, code, name) VALUES (6, 'PAYMENT_FULFILLED', 'Выплата произведена');
INSERT INTO insurance_status(id, code, name) VALUES (7, 'CHANGING_APPLICATION_RECEIVED', 'Получено заявление на изменение договора');
INSERT INTO insurance_status(id, code, name) VALUES (8, 'REFUSING_APPLICATION_RECEIVED', 'Получено заявление об отказе');
INSERT INTO insurance_status(id, code, name) VALUES (9, 'CANCELLATION_APPLICATION_RECEIVED', 'Получено заявление о расторжении договора');
INSERT INTO insurance_status(id, code, name) VALUES (10, 'CANCELED', 'Расторгнут');
INSERT INTO insurance_status(id, code, name) VALUES (11, 'REVOKED', 'Аннулирован');
INSERT INTO insurance_status(id, code, name) VALUES (12, 'FINISHED', 'Окончен');
