--liquibase formatted sql

--changeset pue1etproof:drop-notice-image-table
--comment: 공지사항 이미지 테이블 삭제

drop table notice_image;

--rollback create table notice_image
--rollback (
--rollback    id         bigserial primary key not null,
--rollback    notice_id  bigserial             not null,
--rollback    path       varchar(255)          not null,
--rollback    created_at timestamp             not null default now(),
--rollback    created_by varchar(255)          not null,
--rollback    updated_at timestamp             not null default now(),
--rollback    updated_by varchar(255)          not null
--rollback );