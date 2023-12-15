set search_path to 'insurance_service';

--02
ALTER TABLE contract ADD COLUMN IF NOT EXISTS code VARCHAR(6);
ALTER TABLE contract ADD COLUMN IF NOT EXISTS cashier_id BIGINT;
ALTER TABLE contract ADD COLUMN IF NOT EXISTS cashier_personnel_number VARCHAR(20);
ALTER TABLE contract ADD COLUMN IF NOT EXISTS cashier_name VARCHAR(767);
ALTER TABLE contract ADD COLUMN IF NOT EXISTS cashier_branch_name VARCHAR(255);
ALTER TABLE contract ADD COLUMN IF NOT EXISTS cashier_office_name VARCHAR(255);

--03
insert into rights_to_permission(permission_id, right_id)
SELECT 'create-contract', 'CREATE_CONTRACT' WHERE NOT EXISTS (
    SELECT permission_id
    FROM rights_to_permission
    WHERE permission_id = 'create-contract'
      and right_id = 'CREATE_CONTRACT'
);

insert into rights_to_permission(permission_id, right_id)
SELECT 'create-contract', 'CREATE_CONTRACT_IN_CALL_CENTER' WHERE NOT EXISTS (
     SELECT permission_id
     FROM rights_to_permission
     WHERE permission_id = 'create-contract'
       and right_id = 'CREATE_CONTRACT_IN_CALL_CENTER'
);

--04

ALTER TABLE insurance_service.contract ADD IF NOT EXISTS subdivision_id  BIGINT;

DO $$DECLARE r record;
BEGIN
  FOR r IN SELECT id FROM insurance_service.contract WHERE subdivision_id ISNULL
  LOOP
    update insurance_service.contract set subdivision_id = (
	 select id from auth_service.orgunit where NAME = (SELECT c.subdivision_name FROM insurance_service.contract c WHERE c.id = r.id)
	 ) where id = r.id;
  END LOOP;
END$$;