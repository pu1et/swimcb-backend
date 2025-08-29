package com.project.swimcb.survey.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.entity.SurveyResponseEntity;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.db.repository.SurveyResponseDissatisfactionReasonRepository;
import com.project.swimcb.db.repository.SurveyResponseRepository;
import com.project.swimcb.survey.domain.CreateSurveyDissatisfactionResponseCommand;
import com.project.swimcb.survey.domain.SurveyDissatisfactionReason;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateSurveyDissatisfactionResponseInteractorTest {

  @InjectMocks
  private CreateSurveyDissatisfactionResponseInteractor interactor;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private SurveyResponseRepository surveyResponseRepository;

  @Mock
  private SurveyResponseDissatisfactionReasonRepository surveyResponseDissatisfactionReasonRepository;

  @Nested
  @DisplayName("불만족 설문 응답 생성")
  class CreateSurveyDissatisfactionResponse {

    @Test
    @DisplayName("성공적으로 불만족 설문 응답을 생성한다")
    void createSurveyDissatisfactionResponseSuccessfully() {
      // given
      val memberId = 1L;
      val feedback = "앱이 사용하기 어려워요";
      val reasons = List.of(
          SurveyDissatisfactionReason.OPERATION_COMPLEX,
          SurveyDissatisfactionReason.TOUCH_HARD
      );
      val command = new CreateSurveyDissatisfactionResponseCommand(
          memberId, feedback, reasons
      );

      val mockMember = mock(MemberEntity.class);
      val mockSurveyResponse = mock(SurveyResponseEntity.class);

      given(memberRepository.findById(memberId)).willReturn(Optional.of(mockMember));
      given(surveyResponseRepository.save(any(SurveyResponseEntity.class)))
          .willReturn(mockSurveyResponse);

      // when
      interactor.createSurveyDissatisfactionResponse(command);

      // then
      then(memberRepository).should().findById(memberId);
      then(surveyResponseRepository).should().save(any(SurveyResponseEntity.class));
      then(surveyResponseDissatisfactionReasonRepository).should()
          .saveAll(anyList());
    }

    @Test
    @DisplayName("memberId가 null인 경우 회원을 찾지 않고 불만족 응답을 생성한다")
    void createSurveyDissatisfactionResponseWithNullMemberId() {
      // given
      val feedback = "앱이 사용하기 어려워요";
      val reasons = List.of(
          SurveyDissatisfactionReason.OPERATION_COMPLEX
      );
      CreateSurveyDissatisfactionResponseCommand command = new CreateSurveyDissatisfactionResponseCommand(
          null, feedback, reasons
      );

      val mockSurveyResponse = mock(SurveyResponseEntity.class);
      given(surveyResponseRepository.save(any(SurveyResponseEntity.class)))
          .willReturn(mockSurveyResponse);

      // when
      interactor.createSurveyDissatisfactionResponse(command);

      // then
      then(memberRepository).shouldHaveNoInteractions();
      then(surveyResponseRepository).should().save(any(SurveyResponseEntity.class));
      then(surveyResponseDissatisfactionReasonRepository).should()
          .saveAll(anyList());
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 요청하면 NoSuchElementException이 발생한다")
    void throwsNoSuchElementExceptionWhenMemberNotFound() {
      // given
      val nonExistentMemberId = 999L;
      val feedback = "앱이 사용하기 어려워요";
      val reasons = List.of(
          SurveyDissatisfactionReason.OPERATION_COMPLEX
      );
      val command = new CreateSurveyDissatisfactionResponseCommand(
          nonExistentMemberId, feedback, reasons
      );

      given(memberRepository.findById(nonExistentMemberId)).willReturn(Optional.empty());

      // when
      // then
      assertThatThrownBy(() -> interactor.createSurveyDissatisfactionResponse(command))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessage("해당 회원이 존재하지 않습니다 : " + nonExistentMemberId);

      then(memberRepository).should().findById(nonExistentMemberId);
      then(surveyResponseRepository).shouldHaveNoInteractions();
      then(surveyResponseDissatisfactionReasonRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("command가 null이면 NullPointerException이 발생한다")
    void throwsNullPointerExceptionWhenCommandIsNull() {
      // given
      val nullCommand = (CreateSurveyDissatisfactionResponseCommand) null;

      // when & then
      assertThatThrownBy(() -> interactor.createSurveyDissatisfactionResponse(nullCommand))
          .isInstanceOf(NullPointerException.class);

      then(memberRepository).shouldHaveNoInteractions();
      then(surveyResponseRepository).shouldHaveNoInteractions();
      then(surveyResponseDissatisfactionReasonRepository).shouldHaveNoInteractions();
    }

  }

}
