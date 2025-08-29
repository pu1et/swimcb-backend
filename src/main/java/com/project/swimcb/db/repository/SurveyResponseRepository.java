package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SurveyResponseEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponseEntity, Long> {

  Optional<SurveyResponseEntity> findByMember_Id(@NonNull Long memberId);

}
