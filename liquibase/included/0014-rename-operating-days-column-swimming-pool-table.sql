--liquibase formatted sql

--changeset pu1etproof:rename-operating-days-column-swimming-pool-table
--comment: 수영장 테이블의 operating_day 컬럼명을 operating_days로 변경

alter table swimming_pool rename column operating_day to operating_days;

--rollback alter table swimming_pool rename column operating_days to operating_day;
