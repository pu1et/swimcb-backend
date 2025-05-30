--liquibase formatted sql

--changeset pu1etproof:add-swimming-class-id-column-reservation-table
--comment: 예약 테이블에 수영 클래스 ID 추가

alter table reservation add column swimming_class_id bigint;

--rollback alter table reservation drop column swimming_class_id;
