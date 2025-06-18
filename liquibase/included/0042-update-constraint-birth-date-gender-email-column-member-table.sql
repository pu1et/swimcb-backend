--liquibase formatted sql

--changeset pu1etproof:update-constraint-birth-date-gender-email-column-member-table
--comment: 생일, 성별, 이메일 컬럼 제약조건 수정

ALTER TABLE member ALTER COLUMN birth_date DROP NOT NULL;
ALTER TABLE member ALTER COLUMN gender DROP NOT NULL;
ALTER TABLE member ALTER COLUMN email SET NOT NULL;

--rollback ALTER TABLE member ALTER COLUMN email DROP NOT NULL;
--rollback ALTER TABLE member ALTER COLUMN gender SET NOT NULL;
--rollback ALTER TABLE member ALTER COLUMN birth_date SET NOT NULL;
