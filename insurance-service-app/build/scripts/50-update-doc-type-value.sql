set search_path to 'insurance_service';

update insurance_service.documents_for_client set doc_type = 'PASSPORT_RF' where doc_type = 'Паспорт Рф';