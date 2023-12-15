set search_path to 'insurance_service';

CREATE TABLE IF NOT EXISTS statements (
    id                BIGSERIAL      NOT NULL,
    insurance_id      BIGINT         NOT NULL       references insurance (id),
    insurance_status  BIGINT         NOT NULL       references insurance_status (id),
    client_id         BIGINT         NOT NULL       references clients (id),
    is_complete       BOOLEAN        NOT NULL,
    comment           VARCHAR(1001),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS statement_attachments (
    id                BIGSERIAL NOT NULL,
    attachment_id     VARCHAR(36)                   references attachments (id),
    statement_id      BIGINT                        references statements (id),
    is_deleted        BOOLEAN,
    PRIMARY KEY (ID)
);
