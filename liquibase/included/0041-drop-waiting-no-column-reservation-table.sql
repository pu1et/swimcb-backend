--liquibase formatted sql

--changeset pu1etproof:drop-waiting-no-column-reservation-table
--comment: 예약 테이블에 대기열 관련 컬럼 제거

alter table reservation drop column waiting_no;

--rollback alter table reservation add column waiting_no bigint;
