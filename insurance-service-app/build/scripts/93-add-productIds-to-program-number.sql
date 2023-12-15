set search_path to 'insurance_service';

update program_v2 set number = '55'
where id = (select  id from program_v2 where name = 'Медсовтеник. Максимальный');

update program_v2 set number = '56'
where id = (select  id from program_v2 where name = 'Медсоветник +. Демо-версия');

update program_v2 set number = '58'
where id = (select  id from program_v2 where name = 'Медсоветник +. Максимальный');

update program_v2 set number = '57'
where id = (select  id from program_v2 where name = 'Медсоветник +. Оптимум');

update program_v2 set number = '53'
where id = (select  id from program_v2 where name = 'Медсоветник. Индивидуальный');

update program_v2 set number = '54'
where id = (select  id from program_v2 where name = 'Медсоветник. Семейный (1+1)');