--liquibase formatted sql

--changeset pu1etproof:add-related-payment-refund-column-reservation-table
--comment: 예약 테이블에 관련 결제 환불 컬럼 추가

alter table reservation add column payment_amount int not null default 0;
alter table reservation add column payment_approved_at timestamp;
alter table reservation add column canceled_at timestamp;
alter table reservation add column refund_amount int;
alter table reservation add column refunded_at timestamp;

--rollback alter table reservation drop column refunded_at;
--rollback alter table reservation drop column refund_amount;
--rollback alter table reservation drop column canceled_at;
--rollback alter table reservation drop column payment_approved_at;
--rollback alter table reservation drop column payment_amount;
