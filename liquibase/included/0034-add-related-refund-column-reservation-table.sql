--liquibase formatted sql

--changeset pu1etproof:add-related-refund-column-reservation-table
--comment: 예약 테이블에 환불 관련 계좌번호 및 은행명 컬럼 추가

alter table reservation add column refund_account_no varchar(50);
alter table reservation add column refund_bank_name varchar(50);

--rollback alter table reservation drop column refund_bank_name;
--rollback alter table reservation drop column refund_account_no;
