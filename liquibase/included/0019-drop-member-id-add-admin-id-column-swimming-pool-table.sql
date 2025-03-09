--liquibase formatted sql

--changeset pu1etproof:drop-member-id-add-admin-id-column-swimming-pool-table
--comment: 수영장 테이블에 회원 아이디 컬럼 삭제 및 관리자 아이디 컬럼 추가

alter table swimming_pool drop column member_id;
alter table swimming_pool add column admin_id bigint;
alter table swimming_pool add constraint fk_swimming_pool_admin_id foreign key (admin_id) references admin (id);

--rollback alter table swimming_pool drop column admin_id;
--rollback alter table swimming_pool add column member_id bigint not null default 1;
--rollback alter table swimming_pool add constraint fk_swimming_pool_member_id foreign key (member_id) references member (id)