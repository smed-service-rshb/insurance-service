set search_path to 'insurance_service';

UPDATE insurance ins
SET conclusionDate = (
	SELECT sh.change_date
	FROM insurance_status_history sh
	WHERE sh.insurance_id = ins.id AND (sh.status_id = 17 OR sh.status_id = 3)
	limit 1
)
WHERE
	ins.id in (SELECT ins.id
	FROM INSURANCE ins
	INNER JOIN insurance_status_history sh ON sh.insurance_id = ins.id
	INNER JOIN program_setting ps ON ps.id = ins.program_setting_id
	INNER JOIN program_V2 p ON p.id = program
	WHERE
		ins.conclusionDate IS NULL
		AND ins.creation_date BETWEEN TIMESTAMP '2019-02-01' AND '2019-02-06'
		AND ((sh.status_id = 17 AND p.type = 'ISJ') OR (sh.status_id = 3 AND p.type = 'KSP')));
