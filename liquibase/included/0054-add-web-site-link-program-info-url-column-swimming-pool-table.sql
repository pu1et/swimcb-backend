--liquibase formatted sql

--changeset pu1etproof:0054-add-web-site-link-program-info-url-column-swimming-pool-table
--comment: 수영장 테이블에 웹사이트 링크, 프로그램 정보 링크 컬럼 추가

alter table swimming_pool add column website_url varchar(512);
alter table swimming_pool add column program_info_url varchar(512);

--rollback alter table swimming_pool drop column website_url;
--rollback alter table swimming_pool drop column program_info_url;
