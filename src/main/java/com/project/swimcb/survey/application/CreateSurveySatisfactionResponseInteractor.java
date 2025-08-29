package com.project.swimcb.survey.application;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.entity.SurveyResponseEntity;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.db.repository.SurveyResponseRepository;
import com.project.swimcb.survey.application.port.in.CreateSurveySatisfactionResponseUseCase;
import com.project.swimcb.survey.domain.CreateSurveySatisfactionResponseCommand;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CreateSurveySatisfactionResponseInteractor implements
    CreateSurveySatisfactionResponseUseCase {

  private final MemberRepository memberRepository;
  private final SurveyResponseRepository surveyResponseRepository;

  @Override
  public void createSurveySatisfactionResponse(
      @NonNull CreateSurveySatisfactionResponseCommand command
  ) {

    val member = findMember(command.memberId());
    saveSurveyResponseEntity(command, member);
  }

  private MemberEntity findMember(Long memberId) {
    if (memberId == null) {
      return null;
    }
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다 : " + memberId));
  }

  private void saveSurveyResponseEntity(
      @NonNull CreateSurveySatisfactionResponseCommand command,
      MemberEntity member
  ) {
    val surveyResponseEntity = SurveyResponseEntity.createSatisfaction()
        .member(member)
        .overallRating(command.overallRating())
        .findPoolRating(command.findPoolRating())
        .reservationRating(command.reservationRating())
        .usabilityRating(command.usabilityRating())
        .satisfactionFeedback(command.feedback())
        .build();
    surveyResponseRepository.save(surveyResponseEntity);
  }

}
