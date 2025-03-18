--liquibase formatted sql

--changeset pu1etproof:drop-each-days-and-add-day-of-week-column-swimming-pool-table
--comment: 수영장 테이블에 각 요일 컬럼 삭제 및 일주일을 한번에 표현하는 컬럼 추가

alter table swimming_class drop column is_monday;
alter table swimming_class drop column is_tuesday;
alter table swimming_class drop column is_wednesday;
alter table swimming_class drop column is_thursday;
alter table swimming_class drop column is_friday;
alter table swimming_class drop column is_saturday;
alter table swimming_class drop column is_sunday;

alter table swimming_class add column days_of_week int not null default 0;

--rollback alter table swimming_class drop column days_of_week;

--rollback alter table swimming_class add column is_monday boolean not null;
--rollback alter table swimming_class add column is_tuesday boolean not null;
--rollback alter table swimming_class add column is_wednesday boolean not null;
--rollback alter table swimming_class add column is_thursday boolean not null;
--rollback alter table swimming_class add column is_friday boolean not null;
--rollback alter table swimming_class add column is_saturday boolean not null;
--rollback alter table swimming_class add column is_sunday boolean not null;