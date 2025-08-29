package com.project.swimcb.survey.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.entity.SurveyResponseEntity;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.db.repository.SurveyResponseRepository;
import com.project.swimcb.survey.domain.CreateSurveySatisfactionResponseCommand;
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
class CreateSurveySatisfactionResponseInteractorTest {

  @InjectMocks
  private CreateSurveySatisfactionResponseInteractor interactor;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private SurveyResponseRepository surveyResponseRepository;

  @Nested
  @DisplayName("만족도 설문 응답 생성")
  class CreateSurveySatisfactionResponse {

    @Test
    @DisplayName("성공적으로 만족도 설문 응답을 생성한다")
    void createSurveySatisfactionResponseSuccessfully() {
      // given
      val memberId = 1L;
      val overallRating = 4;
      val findPoolRating = 5;
      val reservationRating = 4;
      val usabilityRating = 4;
      val feedback = "앱이 정말 유용하고 사용하기 편리합니다";
      val command = new CreateSurveySatisfactionResponseCommand(
          memberId, overallRating, findPoolRating, reservationRating, usabilityRating, feedback
      );

      val mockMember = mock(MemberEntity.class);
      val mockSurveyResponse = mock(SurveyResponseEntity.class);

      given(memberRepository.findById(memberId)).willReturn(Optional.of(mockMember));
      given(surveyResponseRepository.save(any(SurveyResponseEntity.class)))
          .willReturn(mockSurveyResponse);

      // when
      interactor.createSurveySatisfactionResponse(command);

      // then
      then(memberRepository).should().findById(memberId);
      then(surveyResponseRepository).should().save(any(SurveyResponseEntity.class));
    }

    @Test
    @DisplayName("memberId가 null인 경우 회원을 찾지 않고 만족도 응답을 생성한다")
    void createSurveySatisfactionResponseWithNullMemberId() {
      // given
      val overallRating = 4;
      val findPoolRating = 5;
      val reservationRating = 4;
      val usabilityRating = 4;
      val feedback = "앱이 정말 유용하고 사용하기 편리합니다";
      val command = new CreateSurveySatisfactionResponseCommand(
          null, overallRating, findPoolRating, reservationRating, usabilityRating, feedback
      );

      val mockSurveyResponse = mock(SurveyResponseEntity.class);
      given(surveyResponseRepository.save(any(SurveyResponseEntity.class)))
          .willReturn(mockSurveyResponse);

      // when
      interactor.createSurveySatisfactionResponse(command);

      // then
      then(memberRepository).shouldHaveNoInteractions();
      then(surveyResponseRepository).should().save(any(SurveyResponseEntity.class));
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 요청하면 NoSuchElementException이 발생한다")
    void throwsNoSuchElementExceptionWhenMemberNotFound() {
      // given
      val nonExistentMemberId = 999L;
      val overallRating = 4;
      val findPoolRating = 5;
      val reservationRating = 4;
      val usabilityRating = 4;
      val feedback = "앱이 정말 유용하고 사용하기 편리합니다";
      val command = new CreateSurveySatisfactionResponseCommand(
          nonExistentMemberId, overallRating, findPoolRating, reservationRating, usabilityRating,
          feedback
      );

      given(memberRepository.findById(nonExistentMemberId)).willReturn(Optional.empty());

      // when
      // then
      assertThatThrownBy(() -> interactor.createSurveySatisfactionResponse(command))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessage("해당 회원이 존재하지 않습니다 : " + nonExistentMemberId);

      then(memberRepository).should().findById(nonExistentMemberId);
      then(surveyResponseRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("command가 null이면 NullPointerException이 발생한다")
    void throwsNullPointerExceptionWhenCommandIsNull() {
      // given
      val nullCommand = (CreateSurveySatisfactionResponseCommand) null;

      // when
      // then
      assertThatThrownBy(() -> interactor.createSurveySatisfactionResponse(nullCommand))
          .isInstanceOf(NullPointerException.class);

      then(memberRepository).shouldHaveNoInteractions();
      then(surveyResponseRepository).shouldHaveNoInteractions();
    }

  }

}
