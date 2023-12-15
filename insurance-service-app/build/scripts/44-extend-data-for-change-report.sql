set search_path to 'insurance_service';


INSERT INTO settings(key, description, value)
VALUES ('ChangeReportStorePath', '', '/tmp/share/') ON CONFLICT DO NOTHING;

ALTER TABLE attachments ADD COLUMN IF NOT EXISTS ownerName character varying(254);

-- Заполнение для старых договоров в таблице attachments поля owner тем значением, которое указано в таблице insurance в поле employee_id
-- Для новых договоров это поле будет заполняться при прикреплении автоматически
UPDATE attachments
SET owner = (
  SELECT ins.employee_id
  FROM insurance ins
  WHERE ins.id = contract
), ownername = (
  SELECT ins.employee_name
  FROM insurance ins
  WHERE ins.id = contract
)
WHERE ownername is null OR (owner is null or owner = 0);

DROP VIEW IF EXISTS change_report;
-- Расширение типа поля change_date в таблице insurance_status_history с даты до даты и времени
ALTER TABLE insurance_status_history ALTER COLUMN change_date TYPE timestamp without time zone;


-- Создание вьюхи с отчётом по изенениям
CREATE OR REPLACE VIEW change_report AS
SELECT row_number() OVER (ORDER BY 1) AS id,
       t.contract_number,
       t.conclusion_date,
       t.holder_name,
       t.change_date,
       t.prev_status,
       t.next_status,
       t.attached_doc_type,
       t.full_set,
       t.employee_name
FROM ( SELECT
              nextstatus.id AS status_id,
              ins.contract_number AS contract_number,
              ins.conclusiondate AS conclusion_date,
              (((cl.surname::text || ' '::text) || cl.firstname::text)::text) || COALESCE(' ' || cl.middlename, ''::character varying)::text AS holder_name,
              nextstatus.change_date AS change_date,
              COALESCE(( SELECT prevstatusname.name
                         FROM insurance_status_history prev
                                LEFT JOIN insurance_status prevstatusname ON prev.status_id = prevstatusname.id
                         WHERE prev.insurance_id = nextstatus.insurance_id AND prev.id < nextstatus.id
                         ORDER BY prev.id DESC
                         LIMIT 1), 'Нет статуса'::character varying) AS prev_status,
              statusname.name AS next_status,
              NULL::character varying AS attached_doc_type,
              COALESCE(ins.full_set_document, false) AS full_set,
              nextstatus.employee_name AS employee_name
       FROM
            insurance ins
              LEFT JOIN clients cl ON cl.id = ins.holder_id
              LEFT JOIN insurance_status_history nextstatus ON nextstatus.insurance_id = ins.id
              LEFT JOIN insurance_status statusname ON nextstatus.status_id = statusname.id
       UNION ALL
       SELECT
              NULL::bigint AS status_id,
              ins.contract_number AS contract_number,
              ins.conclusiondate AS conclusion_date,
              (((cl.surname::text || ' '::text) || cl.firstname::text)::text) || COALESCE(' ' || cl.middlename, ''::character varying)::text AS holder_name,
              attach.createdate AS change_date,
              COALESCE(( SELECT prevstatusname.name
                         FROM insurance_status_history prev
                                LEFT JOIN insurance_status prevstatusname ON prev.status_id = prevstatusname.id
                         WHERE prev.insurance_id = nextstatus.insurance_id AND prev.id < nextstatus.id
                         ORDER BY prev.id DESC
                         LIMIT 1), 'Нет статуса'::character varying) AS prev_status,
              COALESCE(statusname.name, 'Нет статуса'::character varying) AS next_status,
              docname.type AS attached_doc_type,
              COALESCE(ins.full_set_document, false) AS full_set,
              attach.ownername::text AS employee_name

       FROM attachments attach
              JOIN required_document docname ON attach.documenttype = docname.id
              LEFT JOIN insurance ins ON attach.contract = ins.id
              LEFT JOIN insurance_status_history nextstatus ON nextstatus.insurance_id = ins.id
                                                                 AND nextstatus.id =
                                                (SELECT MAX(id)
                                                  FROM insurance_status_history st
                                                  WHERE st.change_date < attach.createdate AND st.insurance_id = ins.id )

              LEFT JOIN insurance_status statusname ON nextstatus.status_id = statusname.id
              LEFT JOIN clients cl ON cl.id = ins.holder_id) t
ORDER BY t.contract_number, t.change_date;
