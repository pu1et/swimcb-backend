--liquibase formatted sql

--changeset pu1etproof:add-cancel-reason-canceled-at-column-swimming-class-table
--comment: 수영 클래스 테이블에 cancel_reason, canceled_at 컬럼 추가

alter table swimming_class add column cancel_reason varchar(255);
alter table swimming_class add column canceled_at timestamp;

--rollback alter table swimming_class drop column canceled_at;
--rollback alter table swimming_class drop column cancel_reason;
