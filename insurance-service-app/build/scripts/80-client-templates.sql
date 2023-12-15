set search_path to 'insurance_service';

CREATE OR REPLACE FUNCTION setup_permission_rights(i_permission VARCHAR(255), i_rights varchar[])
    RETURNS void AS
$$
DECLARE
    rightItem varchar;
BEGIN
    FOREACH rightItem IN ARRAY i_rights
        LOOP
            INSERT INTO rights_to_permission(right_id, permission_id)
            VALUES (rightItem, i_permission)
            ON CONFLICT DO NOTHING;
        END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT setup_permission_rights('view-template-content', ARRAY ['VIEW_CLIENT_TEMPLATES_LIST', 'EDIT_CLIENT_TEMPLATES']);


CREATE TABLE IF NOT EXISTS client_template
(
  id          bigserial     NOT NULL,
  kind        VARCHAR(40),
  program_id  BIGINT,
  is_template Boolean,
  name        VARCHAR(100)  NOT NULL,
  description VARCHAR(1000) NOT NULL,
  start_date  DATE          NOT NULL,
  end_date    DATE,
  link        varchar(255),
  attach_id   VARCHAR(40),
  deleted     boolean,


  PRIMARY KEY (id),
  CONSTRAINT client_template_program_v2_fkey FOREIGN KEY (program_id) REFERENCES program_v2 (id),
  CONSTRAINT client_template_attach_id_fkey FOREIGN KEY (attach_id) REFERENCES attachments (id)
);