set search_path to 'insurance_service';

CREATE INDEX IF NOT EXISTS strategy_property_strategy_id_idx
  ON strategy_property(strategy_id);
CREATE INDEX IF NOT EXISTS addresses_for_client_client_id_idx
  ON addresses_for_client(client_id, address_type);
CREATE INDEX IF NOT EXISTS documents_for_client_doc_type_idx
  ON documents_for_client(client_id, doc_type)
  WHERE is_main = TRUE and is_active = TRUE;
CREATE INDEX IF NOT EXISTS insurance_2_recipient_id_idx
  ON insurance_2_recipient(insurance_id);
CREATE INDEX IF NOT EXISTS risk_name_idx
  ON risk(name);
CREATE INDEX IF NOT EXISTS insurance_2_risk_info_insurance_id_idx
  ON insurance_2_risk_info(insurance_id);
CREATE INDEX IF NOT EXISTS insurance_2_risk_info_risk_id_idx
  ON insurance_2_risk_info(risk_id);