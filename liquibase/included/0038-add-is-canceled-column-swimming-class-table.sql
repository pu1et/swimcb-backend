--liquibase formatted sql

--changeset pu1etproof:add-is-canceled-column-swimming-class-table
--comment: 수영 클래스 테이블에 is_canceled 컬럼 추가

alter table swimming_class add column is_canceled boolean not null default false;

--rollback alter table swimming_class drop column is_canceled;
