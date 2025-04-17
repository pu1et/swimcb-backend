--liquibase formatted sql

--changeset pu1etproof:modify-reservation-status-column-size-reservation-table
--comment: 예약 테이블의 reservation_status 컬럼 크기를 20에서 30으로 변경

alter table reservation alter column reservation_status type varchar(30);

--rollback alter table reservation alter column reservation_status type varchar(20);