--liquibase formatted sql

--changeset pue1etproof:add-latitude-longitude-column-swimming-pool-table
--comment: 수영장 테이블에 위도, 경도 컬럼 추가

alter table swimming_pool add column latitude double precision;
alter table swimming_pool add column longitude double precision;

--rollback alter table swimming_pool drop column longitude;
--rollback alter table swimming_pool drop column latitude;