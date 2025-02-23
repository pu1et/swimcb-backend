--liquibase formatted sql

--changeset pue1etproof:create-swimming-pool-table
--comment: 수영장 테이블 생성

create table swimming_pool
(
    id                                bigserial primary key not null,
    member_id                         bigint                not null,
    name                              varchar(100),
    phone                             varchar(20),
    address                           varchar(255),
    new_registration_period_start_day int,
    new_registration_period_end_day   int,
    re_registration_period_start_day  int,
    re_registration_period_end_day    int,
    is_new_registration_extended      boolean               not null,
    operating_day                     varchar(255),
    representative_image_path         varchar(255),
    usage_agreement_path              varchar(255),
    created_at                        timestamp             not null default now(),
    created_by                        varchar(255)          not null,
    updated_at                        timestamp             not null default now(),
    updated_by                        varchar(255)          not null,
    constraint fk_swimming_pool_member_id foreign key (member_id) references member (id)
);

--rollback drop table swimming_pool;