--liquibase formatted sql

--changeset pu1etproof:add-payment-verification-at-column-reservation-table
--comment: 예약 테이블에 입금확인중 시간 컬럼 추가

alter table reservation add column payment_verification_at timestamp;

--rollback alter table reservation drop column payment_verification_at;
