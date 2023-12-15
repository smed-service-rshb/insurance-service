set search_path to 'insurance_service';

-- Добавление индексов, которые должны ускорить формирование списка договоров страхования
CREATE INDEX IF NOT EXISTS insurance_not_deleted_idx
ON insurance(id)
WHERE deleted = FALSE;

CREATE INDEX IF NOT EXISTS documents_for_client_main_active_idx
ON documents_for_client(client_id)
WHERE is_main = TRUE and is_active = TRUE;

CREATE INDEX IF NOT EXISTS phones_for_client_main_idx
ON phones_for_client(client_id)
WHERE main = TRUE;