set search_path to 'insurance_service';

CREATE TABLE IF NOT EXISTS orders (
   id                   BIGSERIAL NOT NULL,
   creation_date        TIMESTAMP NOT NULL,
   contract_id          BIGINT,
   client_id            BIGINT,
   amount               BIGINT,
   currency_iso_code    INT,
   ext_id               VARCHAR(254),
   redirect_url         VARCHAR(513),
   order_code           INT,
   error_code           INT,
   error_message        VARCHAR(513),
   pan                  VARCHAR(19),
   expiration           VARCHAR(6),
   card_holder_name     VARCHAR(64),
   ip                   VARCHAR(20),
   modified_date        TIMESTAMP NOT NULL,
   PRIMARY KEY (ID)
);
