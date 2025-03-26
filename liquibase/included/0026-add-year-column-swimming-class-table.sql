--liquibase formatted sql

--changeset pu1etproof:add-year-column-swimming-class-table
--comment: 수영 클래스 테이블에 year 컬럼 추가

alter table swimming_class add column year int not null default 2025;

--rollback alter table swimming_class drop column year;
