--liquibase formatted sql

--changeset pu1etproof:create-swimming-pool-rating-table
--comment: 수영장 별점 테이블 생성

create table swimming_pool_rating
(
    id               bigserial primary key not null,
    member_id        bigint                not null,
    swimming_pool_id bigint                not null,
    rating           int                   not null,
    created_at       timestamp             not null default now(),
    created_by       varchar(255)          not null,
    updated_at       timestamp             not null default now(),
    updated_by       varchar(255)          not null,
    constraint fk_swimming_pool_rating_member_id foreign key (member_id) references member (id),
    constraint fk_swimming_pool_rating_swimming_pool_id foreign key (swimming_pool_id) references swimming_pool (id)
);

--rollback drop table swimming_pool_rating;