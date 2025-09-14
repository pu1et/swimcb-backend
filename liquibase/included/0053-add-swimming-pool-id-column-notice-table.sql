--liquibase formatted sql

--changeset pu1etproof:0053-add-swimming-pool-id-column-notice-table
--comment: 공지사항 테이블에 수영장 ID 컬럼 추가

alter table notice add column swimming_pool_id bigint not null;

--rollback alter table notice drop column swimming_pool_id;
