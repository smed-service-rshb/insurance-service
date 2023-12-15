set search_path to 'insurance_service';

DELETE FROM programKind_avail;

INSERT INTO programkind_avail(id, programkind, isactive)
VALUES (1, 'SMS', true);