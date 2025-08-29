--liquibase formatted sql

--changeset pu1etproof:0050-modify-overall-rating-survey-response-table
--comment: 설문지 응답 테이블의 overall_rating 컬럼을 numeric(2, 1)에서 smallint로 변경

alter table survey_response alter column overall_rating type smallint;

--rollback alter table survey_response alter column overall_rating type numeric(2, 1);
