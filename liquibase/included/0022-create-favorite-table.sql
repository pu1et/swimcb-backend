--liquibase formatted sql

--changeset pu1etproof:create-favorite-table
--comment: 즐겨찾기 테이블 생성

create table favorite
(
    id          bigserial primary key not null,
    member_id   bigint                not null,
    target_id   bigint                not null,
    target_type varchar(20)           not null,
    created_at  timestamp             not null default now(),
    created_by  varchar(255)          not null,
    updated_at  timestamp             not null default now(),
    updated_by  varchar(255)          not null,
    constraint fk_favorite_member_id foreign key (member_id) references member (id)
);

--rollback drop table favorite;