
DELETE FROM statement_attachments;
DELETE FROM statements;
DELETE FROM user_template;

DELETE FROM request_attachments;
DELETE FROM request;
DELETE FROM topic_request;

DELETE FROM programkind_avail;

DELETE FROM client_template;
DELETE FROM attachments;

DELETE FROM orders;
DELETE FROM program_related_group;

DELETE FROM optional_risk_2_program_setting;
DELETE FROM risk_2_program_setting;
DELETE FROM required_document_setting_2_program_setting;

DELETE FROM required_field_2_program_setting;
DELETE FROM document_template_2_program_setting;
DELETE FROM strategy_2_program_setting;
DELETE FROM acquiring_info;

DELETE FROM rights_to_permission;

DELETE FROM document_template_2_acquiring_program;
delete from insurance_2_risk_info;
delete from insurance_2_add_risk_info;
--delete from insurance_2_recipient_2_risk;
delete from insurance_2_recipient;
delete from insurance_2_buyout;
DELETE from insurance_status_history;
DELETE from insurance;
DELETE FROM acquiring_program;
DELETE FROM images;
DELETE from insurance_status;

DELETE FROM ADDRESSES_FOR_CLIENT;
DELETE FROM phones_for_client;
DELETE FROM documents_for_client;
DELETE FROM client_check;
DELETE FROM clients;
DELETE FROM contract_number_sequence;
DELETE FROM redemption_coefficient;
DELETE FROM redemption;
DELETE FROM program_setting;
DELETE FROM required_document_setting;
DELETE FROM program_related_offices;
DELETE FROM program_v2;
DELETE FROM segment;
DELETE FROM strategy_property;
DELETE FROM quote;
DELETE FROM share_2_strategy;
DELETE FROM base_index_2_strategy;
DELETE FROM share;
DELETE FROM base_index;
DELETE FROM strategy;

DELETE FROM required_document;
DELETE FROM required_field;

DELETE FROM risk_setting;
DELETE FROM risk;
DELETE FROM extract;


DROP INDEX IF EXISTS insurance_2_recipient_uniq;
DROP INDEX IF EXISTS insurance_2_risk_info_uniq;
DROP INDEX IF EXISTS redemption_uniq;
DROP INDEX IF EXISTS redemption_coefficient_uniq;
DROP INDEX IF EXISTS insurance_unique_contract_number;

delete from addresses_for_client_AUD;
delete from documents_for_client_AUD;
delete from phones_for_client_AUD;
delete from clients_AUD;
delete from risk_AUD;
delete from insurance_2_recipient_AUD;
delete from insurance_2_risk_info_AUD;
delete from insurance_AUD;
delete from insurance_status_AUD;
delete from insurance_2_buyout_AUD;
delete from strategy_AUD;
delete from strategy_property_AUD;
delete from client_employees_AUD;
delete from audit_envers_info;

DROP ALIAS IF EXISTS  TO_NUMBER;