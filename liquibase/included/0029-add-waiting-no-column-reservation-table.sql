--liquibase formatted sql

--changeset pu1etproof:add-waiting-no-column-reservation-table
--comment: 예약 테이블에 대기번호 컬럼 추가

alter table reservation add column waiting_no int;

--rollback alter table reservation drop column waiting_no;
