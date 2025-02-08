--liquibase formatted sql

--changeset pue1etproof:create-notice-image-table
--comment: 공지사항 이미지 테이블 생성

create table notice_image
(
    id         bigserial primary key not null,
    notice_id  bigserial             not null,
    path       varchar(255)          not null,
    created_at timestamp             not null default now(),
    created_by varchar(255)          not null,
    updated_at timestamp             not null default now(),
    updated_by varchar(255)          not null
);

--rollback drop table notice_image;