--liquibase formatted sql

--changeset pu1etproof:add-account-no-column-swimming-pool-table
--comment: 수영장 테이블에 계좌번호 컬럼 추가

alter table swimming_pool add column account_no varchar(50);

--rollback alter table swimming_pool drop column account_no;
