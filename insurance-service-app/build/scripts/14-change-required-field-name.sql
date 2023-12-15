set search_path to 'insurance_service';

UPDATE required_field set strid = 'riskLevelDesc' where strid='riskLevel';
COMMIT ;