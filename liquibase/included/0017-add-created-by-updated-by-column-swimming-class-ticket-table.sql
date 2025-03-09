--liquibase formatted sql

--changeset pu1etproof:add-created-by-updated-by-column-swimming-class-ticket-table
--comment: 수영 클래스 티켓 테이블에 생성자, 생성일, 수정자, 수정일 컬럼 추가

alter table swimming_class_ticket add column created_at timestamp not null default now();
alter table swimming_class_ticket add column created_by varchar(255) not null;
alter table swimming_class_ticket add column updated_at timestamp not null default now();
alter table swimming_class_ticket add column updated_by varchar(255) not null;

-- rollback alter table swimming_class_ticket drop column created_at;
-- rollback alter table swimming_class_ticket drop column created_by;
-- rollback alter table swimming_class_ticket drop column updated_at;
-- rollback alter table swimming_class_ticket drop column updated_by;