--liquibase formatted sql

--changeset pu1etproof:set-not-null-admin-id-column-swimming-pool-table
--comment: 수영장 테이블에 관리자 아이디 컬럼 not null 설정

alter table swimming_pool alter column admin_id set not null;

--rollback alter table swimming_pool alter column admin_id drop not null;
