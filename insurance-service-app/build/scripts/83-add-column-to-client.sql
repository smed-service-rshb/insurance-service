set search_path to 'insurance_service';

-- признак согласия на электронный документооборот
ALTER TABLE insurance_service.clients ADD COLUMN IF NOT EXISTS workflowAgreements boolean default false;

ALTER TABLE insurance_service.clients_aud ADD COLUMN IF NOT EXISTS workflowAgreements boolean default false;


