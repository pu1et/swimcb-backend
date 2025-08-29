package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SurveyResponseDissatisfactionReasonEntity;
import com.project.swimcb.db.entity.SurveyResponseDissatisfactionReasonEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyResponseDissatisfactionReasonRepository extends
    JpaRepository<SurveyResponseDissatisfactionReasonEntity, SurveyResponseDissatisfactionReasonEntityId> {

}
