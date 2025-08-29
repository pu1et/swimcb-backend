package com.project.swimcb.survey.application;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.entity.SurveyResponseDissatisfactionReasonEntity;
import com.project.swimcb.db.entity.SurveyResponseEntity;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.db.repository.SurveyResponseDissatisfactionReasonRepository;
import com.project.swimcb.db.repository.SurveyResponseRepository;
import com.project.swimcb.survey.application.port.in.CreateSurveyDissatisfactionResponseUseCase;
import com.project.swimcb.survey.domain.CreateSurveyDissatisfactionResponseCommand;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CreateSurveyDissatisfactionResponseInteractor implements
    CreateSurveyDissatisfactionResponseUseCase {

  private final MemberRepository memberRepository;
  private final SurveyResponseRepository surveyResponseRepository;
  private final SurveyResponseDissatisfactionReasonRepository surveyResponseDissatisfactionReasonRepository;

  @Override
  public void createSurveyDissatisfactionResponse(
      @NonNull CreateSurveyDissatisfactionResponseCommand command
  ) {

    val member = findMember(command.memberId());
    val saveEntity = saveSurveyResponseEntity(command, member);
    saveSurveyResponseDissatisfactionReason(command, saveEntity);
  }

  private MemberEntity findMember(Long memberId) {
    if (memberId == null) {
      return null;
    }
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다 : " + memberId));
  }

  private SurveyResponseEntity saveSurveyResponseEntity(
      @NonNull CreateSurveyDissatisfactionResponseCommand command,
      MemberEntity member
  ) {
    val surveyResponseEntity = SurveyResponseEntity.createDissatisfaction()
        .member(member)
        .dissatisfactionFeedback(command.feedback())
        .build();
    return surveyResponseRepository.save(surveyResponseEntity);
  }

  private void saveSurveyResponseDissatisfactionReason(
      @NonNull CreateSurveyDissatisfactionResponseCommand command,
      @NonNull SurveyResponseEntity saveEntity
  ) {
    val dissatisfactionReasonEntities = command.reasons()
        .stream()
        .map(i -> SurveyResponseDissatisfactionReasonEntity.create()
            .surveyResponseEntity(saveEntity)
            .surveyDissatisfactionReason(i)
            .build())
        .toList();
    surveyResponseDissatisfactionReasonRepository.saveAll(dissatisfactionReasonEntities);
  }

}
