--liquibase formatted sql

--changeset pue1etproof:remove-representative-image-path-column-swimming-pool-table
--comment: 수영장 테이블에서 대표 이미지 경로 컬럼 제거

alter table swimming_pool drop column representative_image_path;

--rollback alter table swimming_pool add column representative_image_path varchar(255);