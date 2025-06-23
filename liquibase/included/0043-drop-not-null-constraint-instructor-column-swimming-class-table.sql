--liquibase formatted sql

--changeset pu1etproof:drop-not-null-constraint-instructor-column-swimming-class-table
--comment: 수영 클래스 테이블의 수영 강사 컬럼에서 NOT NULL 제약 조건 제거

alter table swimming_class alter column swimming_instructor_id drop not null;

--rollback alter table swimming_class alter column swimming_instructor_id set not null;
