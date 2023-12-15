set search_path to 'insurance_service';

ALTER TABLE insurance_service.strategy
  ADD column if not exists type VARCHAR(40) NULL;
ALTER TABLE insurance_service.strategy_aud
  ADD column if not exists type VARCHAR(40) NULL;
ALTER TABLE insurance_service.strategy_property
  ADD column if not exists base_index_source_strategy_id BIGINT;
ALTER TABLE insurance_service.strategy_property_aud
  ADD column if not exists base_index_source_strategy_id BIGINT;

create table share (
  id   BIGSERIAL    NOT NULL,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT share_pkey PRIMARY KEY (ID)
);

create table share_2_strategy (
  share_id    BIGINT NOT NULL,
  strategy_id BIGINT NOT NULL,
  UNIQUE (share_id, strategy_id),
  CONSTRAINT share_share_2_strategy_fkey FOREIGN KEY (share_id) REFERENCES share (id),
  CONSTRAINT strategy_share_2_strategy_fkey FOREIGN KEY (strategy_id) REFERENCES strategy (id)
);

create table quote (
  share_id BIGINT  NOT NULL,
  date     DATE    NOT NULL,
  value    NUMERIC NOT NULL,
  CONSTRAINT quote_pk PRIMARY KEY (date, share_id),
  CONSTRAINT share_quote_fkey FOREIGN KEY (share_id) REFERENCES share (id)
);

CREATE TABLE insurance_service.base_index
(
  date          DATE         NOT NULL,
  value         NUMERIC      NOT NULL,
  strategy_name varchar(255) NOT NULL,
  CONSTRAINT base_index_pk PRIMARY KEY (date, strategy_name)
);
create table base_index_2_strategy (
  base_index_date date   NOT NULL,
  strategy_name   varchar(255),
  strategy_id     BIGINT NOT NULL,
  CONSTRAINT strategy_base_index_2_strategy_fkey FOREIGN KEY (strategy_id) REFERENCES strategy (id),
  unique (base_index_date, strategy_name, strategy_id)
);

insert into settings (key, value, description)
values ('BASE_INDEX_PATH', '/tmp/share/baseIndex.xlsx', 'путь к файлу с базовыми индексами стратегии для репликации');
insert into settings (key, value, description)
values ('replicateBaseIndexEnable', 'true', 'Доступность автоматической репликации справочника базовых индексов');
insert into settings (key, value, description)
values ('SHARE_PATH', '/tmp/share/share.xlsx', 'путь к файлу с котировками акций для репликации');
insert into settings (key, value, description)
values ('replicateShareEnable', 'true', 'Доступность автоматической репликации котировок акций');