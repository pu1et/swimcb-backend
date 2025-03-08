--liquibase formatted sql

--changeset pue1etproof:drop-price-column-swimming-class-sub-type-table
--comment: 수영장 강습구분 테이블에서 price 컬럼 제거

alter table swimming_class_sub_type drop column price;

--rollback alter table swimming_class_sub_type add column price int;