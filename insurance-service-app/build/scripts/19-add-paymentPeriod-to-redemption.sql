set search_path to 'insurance_service';
-- добавление поля "Периодичность расчета выкупных сумм" в справочник выкупных сумм
ALTER TABLE redemption ADD payment_period varchar(15);
-- переносим значения из поля периодичность уплаты взносов
CREATE OR REPLACE FUNCTION update_redamption(periods varchar[])
  RETURNS void AS $$
DECLARE
  period varchar;
BEGIN
  FOREACH period IN ARRAY periods LOOP
  update redemption set payment_period = period where periodicity = period;
END LOOP;
END;
$$ LANGUAGE plpgsql;

select update_redamption(ARRAY ['ONCE', 'MONTHLY', 'QUARTERLY', 'TWICE_A_YEAR', 'YEARLY']);

drop FUNCTION if exists update_redamption(varchar[]);