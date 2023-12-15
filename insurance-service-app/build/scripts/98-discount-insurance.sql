alter table insurance_service.program_setting
    add column if not exists discount numeric;

alter table insurance_service.insurance
    add column if not exists premium_without_discount numeric;
alter table insurance_service.insurance_aud
    add column if not exists premium_without_discount numeric;

alter table insurance_service.insurance
    add column if not exists discount numeric;
alter table insurance_service.insurance_aud
    add column if not exists discount numeric;

comment on column insurance_service.insurance.premium_without_discount is 'Премия без скидки';
comment on column insurance_service.insurance_aud.premium_without_discount is 'Премия без скидки';
comment on column insurance_service.insurance.discount is 'Процент скидки';
comment on column insurance_service.insurance_aud.discount is 'Процент скидки';
