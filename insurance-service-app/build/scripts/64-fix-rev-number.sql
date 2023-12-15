set search_path to 'insurance_service';

create or replace function fix_rev_number()
  returns void as $$
declare
  rec record;
begin
  lock table audit_envers_info in exclusive mode;
  alter table audit_envers_info disable trigger all;

  for rec in
    select * from audit_envers_info order by id desc
    loop
      update audit_envers_info set id = id + 1 where id = rec.id;
    end loop;

  for rec in
    select * from addresses_for_client_aud order by rev desc
    loop
      update addresses_for_client_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from documents_for_client_aud order by rev desc
    loop
      update documents_for_client_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from phones_for_client_aud order by rev desc
    loop
      update phones_for_client_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from clients_aud order by rev desc
    loop
      update clients_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from risk_aud order by rev desc
    loop
      update risk_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from insurance_2_recipient_aud order by rev desc
    loop
      update insurance_2_recipient_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from insurance_2_risk_info_aud order by rev desc
    loop
      update insurance_2_risk_info_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from insurance_aud order by rev desc
    loop
      update insurance_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from insurance_status_aud order by rev desc
    loop
      update insurance_status_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from insurance_2_buyout_aud order by rev desc
    loop
      update insurance_2_buyout_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from strategy_aud order by rev desc
    loop
      update strategy_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from strategy_property_aud order by rev desc
    loop
      update strategy_property_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  for rec in
    select * from client_employees_aud order by rev desc
    loop
      update client_employees_aud set rev = rev + 1 where id = rec.id and rev = rec.rev;
    end loop;

  alter table audit_envers_info enable trigger all;

  perform setval(pg_get_serial_sequence('audit_envers_info', 'id'), COALESCE((select MAX(id) + 1 from audit_envers_info), 1), false);

END;
$$ LANGUAGE plpgsql;

select fix_rev_number();

drop function fix_rev_number();