set search_path to 'insurance_service';

delete from required_field where strid in ('area', 'street', 'house', 'apartment', 'registrationPeriodStart', 'registrationPeriodEnd');
INSERT INTO required_field(version, type, strid, name, parent, deleted) VALUES (0, 'FIELD', 'locality', 'Населенный пункт',
 (SELECT id FROM required_field WHERE strid = 'addresses'), false);
