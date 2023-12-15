set search_path to 'insurance_service';

INSERT INTO settings(key, description, value)
VALUES ('UploadChangesResultStorePath', '', '/tmp/share/') ON CONFLICT DO NOTHING;
