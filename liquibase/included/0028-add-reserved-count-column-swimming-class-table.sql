--liquibase formatted sql

--changeset pu1etproof:add-reserved-count-column-swimming-class-table
--comment: 수영 클래스 테이블에 예약된 인원 컬럼 추가

alter table swimming_class add column reserved_count int not null default 0;

--rollback alter table swimming_class drop column reserved_count;
