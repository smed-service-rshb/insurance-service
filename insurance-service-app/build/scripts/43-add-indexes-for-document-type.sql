set search_path to 'insurance_service';

CREATE INDEX IF NOT EXISTS documents_for_client_doc_type_idx
ON documents_for_client (doc_type);