--liquibase formatted sql

--changeset pu1etproof:add-cancellation-reason-account-holder-column-reservation-table
--comment: 예약 테이블에 취소 사유 및 계좌주 컬럼 추가

alter table reservation add column cancellation_reason varchar(50);
alter table reservation add column refund_account_holder varchar(100);

--rollback alter table reservation drop column refund_account_holder;
--rollback alter table reservation drop column cancellation_reason;
