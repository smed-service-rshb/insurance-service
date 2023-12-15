set search_path to 'insurance_service';

SELECT setup_permission_rights('client-view-contract', ARRAY ['CLIENT_VIEW_CONTRACT', 'VIEW_PERSONAL_OFFICE']);
SELECT setup_permission_rights('view-client-template', ARRAY ['VIEW_CLIENT_TEMPLATES_LIST', 'VIEW_PERSONAL_OFFICE']);
SELECT setup_permission_rights('view-client-template', ARRAY ['VIEW_PERSONAL_OFFICE']);