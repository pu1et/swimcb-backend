--liquibase formatted sql

--changeset pu1etproof:create-free-swimming-table:0044
--comment: 자유수영 테이블 생성

create table free_swimming
(
    id               bigserial primary key not null,
    swimming_pool_id bigint                not null,
    year_month       date                  not null,
    days_of_week     int                   not null,
    start_time       time(0) without time zone                  not null,
    end_time         time(0) without time zone                  not null,
    lifeguard_id     bigint,
    capacity         int                   not null,
    is_visible       boolean               not null,
    created_at       timestamp             not null default now(),
    created_by       varchar(255)          not null,
    updated_at       timestamp             not null default now(),
    updated_by       varchar(255)          not null
);

--rollback drop table free_swimming;
