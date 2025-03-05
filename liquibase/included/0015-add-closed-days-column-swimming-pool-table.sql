--liquibase formatted sql

--changeset pue1etproof:add-closed-days-column-swimming-pool-table
--comment: 수영장 테이블에 휴무일 컬럼 추가

alter table swimming_pool add column closed_days varchar(255);

--rollback alter table swimming_pool drop column closed_days;