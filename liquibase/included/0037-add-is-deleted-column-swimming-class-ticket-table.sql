--liquibase formatted sql

--changeset pu1etproof:add-is-deleted-column-swimming-class-ticket-table
--comment: 수영 클래스 티켓 테이블에 is_deleted 컬럼 추가

alter table swimming_class_ticket add column is_deleted boolean not null default false;

--rollback alter table swimming_class_ticket drop column is_deleted;
