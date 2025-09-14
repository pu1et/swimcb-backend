--liquibase formatted sql

--changeset pu1etproof:0052-add-provider-column-member-table
--comment: 회원 테이블에 provider 컬럼 추가

alter table member add column provider varchar(10) not null default 'KAKAO';

--rollback alter table member drop column provider;
