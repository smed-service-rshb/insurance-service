set search_path to 'insurance_service';

update insurance set holder_equals_insured = true where holder_id = insured_id;