set search_path to 'insurance_service';

-- Программа страхования
CREATE TABLE program_v2 (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  name              VARCHAR(50) NOT NULL,
  type              VARCHAR(3) NOT NULL,
  number            VARCHAR(3) NOT NULL,
  policyCode        VARCHAR(3) NOT NULL,
  variant           VARCHAR(2),
  segment           BIGINT,
  coolingPeriod     INTEGER,
  waitingPeriod     INTEGER,
  isActive          BOOLEAN,
  deleted           BOOLEAN default FALSE,

  PRIMARY KEY (ID)
);

-- Параметры программы страхования
CREATE TABLE program_setting (
  id                  BIGSERIAL NOT NULL,
  version             BIGINT NOT NULL,
  program             BIGINT,
  startDate           TIMESTAMP DEFAULT TIMESTAMP 'tomorrow',
  endDate             TIMESTAMP DEFAULT DATE('2999-12-31'),
  policyHolderInsured BOOLEAN,
  minimumTerm         INTEGER,
  maximumTerm         INTEGER,
  calendarUnit        VARCHAR(50),
  currency            BIGINT,
  minSum              NUMERIC,
  maxSum              NUMERIC,
  minPremium          NUMERIC,
  maxPremium          NUMERIC,
  premiumMethod       VARCHAR(50),
  coefficient         NUMERIC,
  bonusAmount         NUMERIC,
  insuranceAmount     NUMERIC,
  tariff              NUMERIC,
  periodicity         VARCHAR(50),
  underwriting        VARCHAR(50),
  minAgeHolder        INTEGER,
  maxAgeHolder        INTEGER,
  minAgeInsured       INTEGER,
  maxAgeInsured       INTEGER,
  minGrowth           INTEGER,
  maxGrowth           INTEGER,
  minWeight           INTEGER,
  maxWeight           INTEGER,
  gender              VARCHAR(50),
  maxUpperPressure    INTEGER,
  minUpperPressure    INTEGER,
  maxLowerPressure    INTEGER,
  minLowerPressure    INTEGER,
  deleted             BOOLEAN default FALSE,
  PRIMARY KEY (ID)
);

-- Сегмент программы страхования
CREATE TABLE segment (
  id              BIGSERIAL NOT NULL,
  version         BIGINT NOT NULL,
  code            VARCHAR(50),
  name            VARCHAR(254),
  isActive        BOOLEAN,
  deleted         BOOLEAN default FALSE,
  PRIMARY KEY (ID)
);


ALTER TABLE program_v2
  ADD CONSTRAINT program_segment_fk
FOREIGN KEY (segment)
REFERENCES segment (id);


-- Стратегия программы страхования
CREATE TABLE strategy (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  name              VARCHAR(50) NOT NULL,
  description       VARCHAR(500),
  policyCode        INTEGER,
  rate              NUMERIC,
  ticker            VARCHAR(20),
  startDate         TIMESTAMP DEFAULT current_timestamp,
  endDate           TIMESTAMP DEFAULT DATE('2999-12-31'),
  deleted           BOOLEAN default FALSE,
  PRIMARY KEY (ID)
);

-- Обязательные документы программы страхования
CREATE TABLE required_document_setting (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  requiredDocument  BIGINT NOT NULL,
  status            VARCHAR(50),
  deleted           BOOLEAN default FALSE,
  PRIMARY KEY (ID)
);

ALTER TABLE required_document_setting
  ADD CONSTRAINT req_doc_setting_req_doc_fk
FOREIGN KEY (requiredDocument)
REFERENCES required_document (id);

-- Обязательные поля программы страхования
CREATE TABLE required_field (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  type              VARCHAR(50) NOT NULL,
  name              VARCHAR(100) NOT NULL,
  strId             VARCHAR(100) NOT NULL,
  parent            BIGINT,
  deleted           BOOLEAN default FALSE,
  PRIMARY KEY (ID)
);

-- Справочник формул программы страхования
CREATE TABLE formula (
  id                BIGSERIAL NOT NULL,
  version           BIGINT NOT NULL,
  scriptFormula     VARCHAR(1000) NOT NULL,
  language          VARCHAR(50) NOT NULL,
  name              VARCHAR(254) NOT NULL,
  deleted           BOOLEAN default FALSE,
  PRIMARY KEY (ID)
);

-- Параметры риска программы страхования
CREATE TABLE risk_setting (
  id                                BIGSERIAL NOT NULL,
  version                           BIGINT NOT NULL,
  riskType                          VARCHAR(50) NOT NULL,
  risk                              BIGINT,
  signAmount                        BOOLEAN,
  minRiskAmount                     NUMERIC,
  maxRiskAmount                     NUMERIC,
  calculationType                   VARCHAR(50),
  riskAmount                        NUMERIC,
  riskDependence                    BIGINT,
  calculationCoefficient            NUMERIC,
  riskPremium                       NUMERIC,
  calculationCoefficientPremium     NUMERIC,
  parentRisk                        BIGINT,
  riskReturnRate                    INTEGER,
  otherRiskParam                    VARCHAR(200),
  insuranceRule                     VARCHAR(200),
  rulesDetails                      VARCHAR(200),
  formula                           BIGINT,
  recordAmount                      VARCHAR(50),
  type                              VARCHAR(50),
  insuranceKind                     VARCHAR(254),
  deleted                           BOOLEAN default FALSE,
  PRIMARY KEY (ID)
);

ALTER TABLE risk_setting
  ADD CONSTRAINT risk_fk
FOREIGN KEY (risk)
REFERENCES risk (id);

-- Связь между параметрами основного риска и параметрами программы страхования
CREATE TABLE risk_2_program_setting (
  riskId              BIGINT,
  programSettingId    BIGINT,
  PRIMARY KEY (riskId, programSettingId)
);

ALTER TABLE risk_2_program_setting
  ADD CONSTRAINT requiredrisk_fk
FOREIGN KEY (riskId)
REFERENCES risk_setting (id);

ALTER TABLE risk_2_program_setting
  ADD CONSTRAINT req_risk_programsetting_fk
FOREIGN KEY (programSettingId)
REFERENCES program_setting (id);

-- Связь между параметрами дополнительного риска и параметрами программы страхования
CREATE TABLE optional_risk_2_program_setting (
  riskId              BIGINT,
  programSettingId    BIGINT,
  PRIMARY KEY (riskId, programSettingId)
);

ALTER TABLE optional_risk_2_program_setting
  ADD CONSTRAINT optionalrisk_fk
FOREIGN KEY (riskId)
REFERENCES risk_setting (id);

ALTER TABLE optional_risk_2_program_setting
  ADD CONSTRAINT opt_risk_programsetting_fk
FOREIGN KEY (programSettingId)
REFERENCES program_setting (id);

-- Связь между обязательным документом и параметрами программы страхования
CREATE TABLE required_document_setting_2_program_setting (
  requiredDocumentSettingId   BIGINT,
  programSettingId            BIGINT,
  PRIMARY KEY (requiredDocumentSettingId, programSettingId)
);

ALTER TABLE required_document_setting_2_program_setting
  ADD CONSTRAINT requireddocuementsetting_fk
FOREIGN KEY (requiredDocumentSettingId)
REFERENCES required_document_setting (id);

ALTER TABLE required_document_setting_2_program_setting
  ADD CONSTRAINT req_doc_programsetting_fk
FOREIGN KEY (programSettingId)
REFERENCES program_setting (id);

-- Связь между обязательным полем и параметрами программы страхования
CREATE TABLE required_field_2_program_setting (
  requiredFieldId            BIGINT,
  programSettingId           BIGINT,
  PRIMARY KEY (requiredFieldId, programSettingId)
);


ALTER TABLE required_field_2_program_setting
  ADD CONSTRAINT requiredfield_fk
FOREIGN KEY (requiredFieldId)
REFERENCES required_field (id);

ALTER TABLE required_field_2_program_setting
  ADD CONSTRAINT req_field_programsetting_fk
FOREIGN KEY (programSettingId)
REFERENCES program_setting (id);

-- Связь между шаблоном документов и параметрами программы страхования
CREATE TABLE document_template_2_program_setting (
  documentTemplateId  VARCHAR(100) NOT NULL,
  programSettingId    BIGINT NOT NULL,
  PRIMARY KEY (documentTemplateId, programSettingId)
);


ALTER TABLE document_template_2_program_setting
  ADD CONSTRAINT doc_templ_programsetting_fk
FOREIGN KEY (programSettingId)
REFERENCES program_setting (id);

-- Связь между стратегией страхования и параметрами программы страхования
CREATE TABLE strategy_2_program_setting (
  strategyId          BIGINT,
  programSettingId    BIGINT,
  PRIMARY KEY (strategyId, programSettingId)
);

ALTER TABLE strategy_2_program_setting
  ADD CONSTRAINT strategy_fk
FOREIGN KEY (strategyId)
REFERENCES strategy (id);


ALTER TABLE strategy_2_program_setting
  ADD CONSTRAINT strategy_programsetting_fk
FOREIGN KEY (programSettingId)
REFERENCES program_setting (id);



--Наполнение стартовыми данными таблицы Обязательных полей
--GENERATING INSTRUCTION:
--STEP 1: FIND REGEX
----(\w+): .*[{|\[] // (.*)
----AND REPLACE TO
----INSERT INTO required_field(version, type, strid, name, parent) VALUES (1, 'FIELD', '\1', '\u\2', null);
--STEP 2: FIND REGEX
----(\w+): .*[,|] // (.*)
----AND REPLACE TO
----INSERT INTO required_field(version, type, strid, name, paren) VALUES (1, 'FIELD', '\1', '\u\2', (SELECT id FROM required_field WHERE strid = ''));

INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD_SET', 'migrationCard', 'Данные миграционной карты (если гражданство - иное)', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD_SET', 'foreignerDoc', 'Данные докумнента, подтв. право пребывания (если гражданство - иное)', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD_SET', 'documents', 'Документы клиента', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD_SET', 'phones', 'Телефоны клиента', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD_SET', 'addresses', 'Адреса клиента', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'docSeries', 'Серия документа', (SELECT id FROM required_field WHERE strid = 'migrationCard'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'docNumber', 'Номер документа', (SELECT id FROM required_field WHERE strid = 'migrationCard'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'stayStartDate', 'Дата начала действия документа', (SELECT id FROM required_field WHERE strid = 'migrationCard'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'stayEndDate', 'Дата окончания действия документа', (SELECT id FROM required_field WHERE strid = 'migrationCard'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'docSeries', 'Серия документа', (SELECT id FROM required_field WHERE strid = 'foreignerDoc'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'docNumber', 'Номер документа', (SELECT id FROM required_field WHERE strid = 'foreignerDoc'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'stayStartDate', 'Дата начала действия документа', (SELECT id FROM required_field WHERE strid = 'foreignerDoc'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'stayEndDate', 'Дата окончания действия документа', (SELECT id FROM required_field WHERE strid = 'foreignerDoc'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'docSeries', 'Серия документа', (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'docNumber', 'Номер документа', (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'issuedBy', 'Кем выдан документ', (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'issuedDate', 'Когда выдан документ', (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'isActive', 'Признак активности', (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'isMain', 'Признак основного документа', (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'divisionCode', 'Код подразделения', (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'issuedEndDate', 'Дата окончания действия документа', (SELECT id FROM required_field WHERE strid = 'documents'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'number', 'Номер телефона', (SELECT id FROM required_field WHERE strid = 'phones'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'main', 'Признак основного номера телефона', (SELECT id FROM required_field WHERE strid = 'phones'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'verified', 'Признак актуальности номера телефона', (SELECT id FROM required_field WHERE strid = 'phones'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'notification', 'Признак согласия на уведомления', (SELECT id FROM required_field WHERE strid = 'phones'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'addressType', 'Тип адреса', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'country', 'Страна', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'region', 'Регион', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'area', 'Область', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'street', 'Улица', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'house', 'Дом', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'apartment', 'Номер квартиры', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'registrationPeriodStart', 'Дата начала регистрации', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'registrationPeriodEnd', 'Дата окончания регистрации', (SELECT id FROM required_field WHERE strid = 'addresses'), false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'surName', 'Фамилия', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'firstName', 'Имя', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'middleName', 'Отчество', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'birthDate', 'Дата рождения', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'citizenship', 'Гражданство (Россия, Беларусь, иное)', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'citizenshipCountry', 'Страна гражданства (если гражданство - иное)', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'gender', 'Пол', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'birthPlace', 'Место рождения', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'residentRF', 'Признак резиденства (резидент, нерезидент)', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'taxResidence', 'Налоговое резидентство (РФ, иностранное гос-во, иное)', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'email', 'Email', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'taxPayerNumber', 'ИНН или иной номер налогоплательщика, если налоговое резидентство - не РФ', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'publicOfficialStatus', 'Признак принадлежности к публичным лицам', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'foreignPublicOfficialType', 'Признак ИПДЛ', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'russianPublicOfficialType', 'Признак РПДЛ', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'relations', 'Степень родства', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'publicOfficialPosition', 'Должность', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'publicOfficialNameAndPosition', 'ФИО, должность', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'beneficialOwner', 'Сведения о бенефициарном владельце', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'businessRelations', 'Сведения о характере деловых отношений', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'activitiesGoal', 'Сведения о целях ФХД', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'businessRelationsGoal', 'Сведения о целях деловых отношений', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'riskLevel', 'Уровень риска', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'businessReputation', 'Сведения о деловой репутации', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'financialStability', 'Сведения о финансовом положении', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'financesSource', 'Сведения об источниках происхождения средств и/или имущества', null, false);
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (1, 'FIELD', 'personalDataConsent', 'Согласие на обработку ПД', null, false);

-- Наполнение данными справочника обязательных документов
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Справка о доходах', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Результаты проведённого медицинского обследования', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Заявление о выплате', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Заявление на изменение договора', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Заявление об отказе', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Заявление о расторжении договора', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Документ, удостоверяющий личность', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Платежное поручение/платежный документ', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Форма самосертификации', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Медицинская анкета', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Заявление на страхование', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Договор страхования', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Уведомление о рисках', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Иное', true, false);
INSERT INTO required_document(version, type, active, deleted) VALUES (0, 'Удаленный', true, true);

-- Наполнение данными справочника сегментов продуктов страхования
INSERT INTO segment(version, code, name, isactive) VALUES (0, 'retail', 'Массовый', true);
INSERT INTO segment(version, code, name, isactive) VALUES (0, 'vip', 'Премиальный', true);

-- Наполнение данными справочника рисков
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'ISJ', 'Дожитие', 'Дожитие', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'ISJ', 'Смерть по любой причине', 'Смерть по любой причине', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'ISJ', 'Смерть в результате несчастного случая', 'Смерть в результате несчастного случая', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'ISJ', 'Смерть в результате кораблекрушения / авиакатастрофы / крушения поезда', 'Смерть в результате кораблекрушения / авиакатастрофы / крушения поезда', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'ISJ', 'Дожитие до окончания Nго года страхования', 'Дожитие до окончания Nго года страхования', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Дожитие', 'Дожитие', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Смерть по любой причине', 'Смерть по любой причине', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Смерть от несчастного случая', 'Смерть от несчастного случая', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Инвалидность I группы', 'Инвалидность I группы', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Травма', 'Травма', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Госпитализация в результате НС', 'Госпитализация в результате НС', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Временная нетрудоспособность в результате НС', 'Временная нетрудоспособность в результате НС', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Инвалидность I, II группы по любой причине', 'Инвалидность I, II группы по любой причине', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Инвалидность I, II, III группы НС', 'Инвалидность I, II, III группы НС', null, null, false);
INSERT INTO risk (version, program_kind, name, full_name, start_date, end_date, deleted) VALUES (0, 'NSJ', 'Инвалидность I, II, III группы по любой причине', 'Инвалидность I, II, III группы по любой причине', null, null, false);

-- Наполнение данными справочника стратегий продуктов страхования
INSERT INTO strategy(version, name, description, policycode, rate, ticker, startdate, enddate, deleted)
VALUES (0, 'Глобальные облигации', null, null, null, null, DATE('2018-08-01'), DATE('2999-12-31'), false);
INSERT INTO strategy(version, name, description, policycode, rate, ticker, startdate, enddate, deleted)
VALUES (0, 'Цифровое будущее', null, null, null, null, DATE('2018-08-01'), DATE('2999-12-31'), false);
INSERT INTO strategy(version, name, description, policycode, rate, ticker, startdate, enddate, deleted)
VALUES (0, 'Европейские лидеры', null, null, null, null, DATE('2018-08-01'), DATE('2999-12-31'), false);
