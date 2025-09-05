--liquibase formatted sql

--changeset pu1etproof:0051-drop-not-null-constraint-member-table
--comment: 회원 테이블의 name, phone, email 컬럼에서 NOT NULL 제약 조건 제거

alter table member alter name drop not null;
alter table member alter phone drop not null;
alter table member alter email drop not null;

--rollback alter table member alter name set not null;
--rollback alter table member alter phone set not null;
--rollback alter table member alter email set not null;
