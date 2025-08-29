package com.project.swimcb.survey.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.project.swimcb.db.entity.SurveyResponseEntity;
import com.project.swimcb.db.repository.SurveyResponseRepository;
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
@DisplayName("CheckMySurveyCompletedInteractor 테스트")
class CheckMySurveyCompletedInteractorTest {

  @InjectMocks
  private CheckMySurveyCompletedInteractor interactor;

  @Mock
  private SurveyResponseRepository surveyResponseRepository;

  @Nested
  @DisplayName("설문 완료 여부 확인")
  class CheckMySurveyCompleted {

    @Test
    @DisplayName("설문을 완료한 회원인 경우 true를 반환한다")
    void returnsTrueWhenMemberCompletedSurvey() {
      // given
      val memberId = 1L;
      val mockSurveyResponse = mock(SurveyResponseEntity.class);

      given(surveyResponseRepository.findByMember_Id(memberId))
          .willReturn(Optional.of(mockSurveyResponse));

      // when
      val result = interactor.checkMySurveyCompleted(memberId);

      // then
      assertThat(result.completed()).isTrue();
      then(surveyResponseRepository).should().findByMember_Id(memberId);
    }

    @Test
    @DisplayName("설문을 완료하지 않은 회원인 경우 false를 반환한다")
    void returnsFalseWhenMemberNotCompletedSurvey() {
      // given
      val memberId = 1L;

      given(surveyResponseRepository.findByMember_Id(memberId))
          .willReturn(Optional.empty());

      // when
      val result = interactor.checkMySurveyCompleted(memberId);

      // then
      assertThat(result.completed()).isFalse();
      then(surveyResponseRepository).should().findByMember_Id(memberId);
    }

    @Test
    @DisplayName("memberId가 null인 경우 NullPointerException이 발생한다")
    void throwsNullPointerExceptionWhenMemberIdIsNull() {
      // given
      val nullMemberId = (Long) null;

      // when
      // then
      assertThatThrownBy(() -> interactor.checkMySurveyCompleted(nullMemberId))
          .isInstanceOf(NullPointerException.class);

      then(surveyResponseRepository).shouldHaveNoInteractions();
    }

  }

}
