set search_path to 'insurance_service';

create table audit_envers_info (
  id bigserial not null,
  timestamp bigint,
  user_id bigint,
  primary key (id)
);

insert into audit_envers_info (id, timestamp) VALUES (0, (extract(epoch from now()) * 1000));

CREATE OR REPLACE FUNCTION create_aud_table(_tbl regclass)
  RETURNS void AS $$
DECLARE
  aud_tbl varchar;
BEGIN
  aud_tbl := _tbl || '_AUD';
  EXECUTE 'create table ' || aud_tbl || ' as select * from '|| _tbl ||';';
  EXECUTE 'ALTER TABLE '|| aud_tbl ||' ADD COLUMN IF NOT EXISTS REV integer not null DEFAULT 0;';
  EXECUTE 'ALTER TABLE '|| aud_tbl ||' ADD COLUMN IF NOT EXISTS REVTYPE integer DEFAULT 0;';
  EXECUTE 'ALTER TABLE '|| aud_tbl ||' ADD PRIMARY KEY (id, REV);';
  EXECUTE 'ALTER TABLE '|| aud_tbl ||' ADD constraint fk_'||aud_tbl||'_REV foreign key (REV) references audit_envers_info;';
END;
$$ LANGUAGE plpgsql;

select create_aud_table('addresses_for_client');
select create_aud_table('documents_for_client');
select create_aud_table('phones_for_client');
select create_aud_table('clients');
select create_aud_table('risk');
select create_aud_table('insurance_2_recipient');
select create_aud_table('insurance_2_risk_info');
select create_aud_table('insurance');
select create_aud_table('insurance_status');
select create_aud_table('insurance_2_buyout');
select create_aud_table('strategy');
select create_aud_table('strategy_property');
select create_aud_table('client_employees');