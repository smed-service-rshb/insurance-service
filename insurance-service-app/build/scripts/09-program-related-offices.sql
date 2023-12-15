set search_path to 'insurance_service';

create table program_related_offices (
  id bigserial not null,
  programId bigint not null,
  name varchar(255) not null,

  primary key (id)
);

alter table program_related_offices
add constraint program_related_offices_program_id_fk
foreign key (programId)
references program_v2(id);

alter table program_v2
add column relatedOfficeFilterType varchar(11);