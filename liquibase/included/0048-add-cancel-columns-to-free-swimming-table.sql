--liquibase formatted sql

--changeset pu1etproof:add-cancel-columns-to-free-swimming-table
--comment: 자유수영 테이블에 취소 관련 컬럼 추가

alter table free_swimming add column is_canceled boolean not null default false;
alter table free_swimming add column cancel_reason varchar(255);
alter table free_swimming add column canceled_at timestamp;

--rollback alter table free_swimming drop column canceled_at;
--rollback alter table free_swimming drop column cancel_reason;
--rollback alter table free_swimming drop column is_cancel;
