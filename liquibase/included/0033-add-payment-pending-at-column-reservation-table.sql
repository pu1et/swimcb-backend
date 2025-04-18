--liquibase formatted sql

--changeset pu1etproof:add-payment-pending-at-column-reservation-table
--comment: 예약 테이블에 결제대기 시간 컬럼 추가

alter table reservation add column payment_pending_at timestamp;

--rollback alter table reservation drop column payment_pending_at;