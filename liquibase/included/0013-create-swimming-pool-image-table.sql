--liquibase formatted sql

--changeset pue1etproof:create-swimming-pool-table
--comment: 수영장 이미지 테이블 생성

create table swimming_pool_image
(
    id               bigserial primary key not null,
    swimming_pool_id bigint                not null,
    path             varchar(255)          not null,
    created_at       timestamp             not null default now(),
    created_by       varchar(255)          not null,
    updated_at       timestamp             not null default now(),
    updated_by       varchar(255)          not null,
    constraint fk_swimming_pool_image_swimming_pool_id foreign key (swimming_pool_id) references swimming_pool (id)
);

--rollback drop table swimming_pool_image;