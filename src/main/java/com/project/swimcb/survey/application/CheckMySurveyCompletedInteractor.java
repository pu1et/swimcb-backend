package com.project.swimcb.survey.application;

import com.project.swimcb.db.repository.SurveyResponseRepository;
import com.project.swimcb.survey.application.port.in.CheckMySurveyCompletedUseCase;
import com.project.swimcb.survey.domain.CheckMySurveyCompleted;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class CheckMySurveyCompletedInteractor implements CheckMySurveyCompletedUseCase {

  private final SurveyResponseRepository surveyResponseRepository;

  @Override
  public CheckMySurveyCompleted checkMySurveyCompleted(@NonNull Long memberId) {
    val isPresent = surveyResponseRepository.findByMember_Id(memberId).isPresent();
    return new CheckMySurveyCompleted(isPresent);
  }

}
