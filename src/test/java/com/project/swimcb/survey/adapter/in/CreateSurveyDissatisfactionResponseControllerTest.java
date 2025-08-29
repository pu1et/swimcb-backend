package com.project.swimcb.survey.adapter.in;

import static com.project.swimcb.survey.adapter.in.CreateSurveyDissatisfactionResponseControllerTest.MEMBER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.survey.application.port.in.CreateSurveyDissatisfactionResponseUseCase;
import com.project.swimcb.survey.domain.SurveyDissatisfactionReason;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CreateSurveyDissatisfactionResponseController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class CreateSurveyDissatisfactionResponseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CreateSurveyDissatisfactionResponseUseCase useCase;

  static final long MEMBER_ID = 1L;

  private static final String PATH = "/api/surveys/dissatisfaction-responses";

  @Nested
  @DisplayName("불만족 평가 설문 응답 등록 API는")
  class CreateSurveyDissatisfactionResponseApi {

    @Test
    @DisplayName("불만족 평가 설문 응답을 성공적으로 등록한다")
    void shouldCreateSurveyDissatisfactionResponseSuccessfully() throws Exception {
      // given
      val request = TestCreateSurveyDissatisfactionResponseRequestFactory.create();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());

      then(useCase).should(only()).createSurveyDissatisfactionResponse(assertArg(i -> {
        assertThat(i.memberId()).isEqualTo(MEMBER_ID);
        assertThat(i.feedback()).isEqualTo(request.feedback());
        assertThat(i.reasons()).isEqualTo(request.reasons());
      }));
    }

    @Test
    @DisplayName("피드백이 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFeedbackIsNull() throws Exception {
      // given
      val request = TestCreateSurveyDissatisfactionResponseRequestFactory.createWithNullFeedback();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("피드백은 null 일 수 없습니다")));
    }

    @Test
    @DisplayName("피드백이 10자 미만인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFeedbackIsTooShort() throws Exception {
      // given
      val request = TestCreateSurveyDissatisfactionResponseRequestFactory.createWithShortFeedback();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("피드백은 10자 이상 500자 이하로 입력해주세요")));
    }

    @Test
    @DisplayName("피드백이 500자 초과인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFeedbackIsTooLong() throws Exception {
      // given
      val request = TestCreateSurveyDissatisfactionResponseRequestFactory.createWithLongFeedback();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("피드백은 10자 이상 500자 이하로 입력해주세요")));
    }

    @Test
    @DisplayName("불만족 사유가 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenReasonsIsNull() throws Exception {
      // given
      val request = TestCreateSurveyDissatisfactionResponseRequestFactory.createWithNullReasons();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("불만족 이유 리스트는 null 일 수 없습니다")));
    }

    @Test
    @DisplayName("불만족 사유가 빈 리스트인 경우 성공적으로 등록한다")
    void shouldCreateSurveyDissatisfactionResponseSuccessfullyWithEmptyReasons() throws Exception {
      // given
      val request = TestCreateSurveyDissatisfactionResponseRequestFactory.createWithEmptyReasons();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("여러 불만족 사유와 함께 성공적으로 등록한다")
    void shouldCreateSurveyDissatisfactionResponseSuccessfullyWithMultipleReasons()
        throws Exception {
      // given
      val request = TestCreateSurveyDissatisfactionResponseRequestFactory.createWithMultipleReasons();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("요청 본문이 비어있는 경우 400 에러를 반환한다")
    void shouldReturn400WhenRequestBodyIsEmpty() throws Exception {
      // given
      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content("{}"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Content-Type이 application/json이 아닌 경우 415 에러를 반환한다")
    void shouldReturn415WhenContentTypeIsNotApplicationJson() throws Exception {
      // given
      val request = TestCreateSurveyDissatisfactionResponseRequestFactory.create();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType("text/plain")
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isUnsupportedMediaType());
    }

  }

  private static class TestCreateSurveyDissatisfactionResponseRequestFactory {

    private static CreateSurveyDissatisfactionResponseRequest create() {
      return new CreateSurveyDissatisfactionResponseRequest(
          "앱이 사용하기 어려워서 개선이 필요합니다",
          List.of(SurveyDissatisfactionReason.SWIMMING_POOL_INACCURATE)
      );
    }

    private static CreateSurveyDissatisfactionResponseRequest createWithNullFeedback() {
      return new CreateSurveyDissatisfactionResponseRequest(
          null,
          List.of(SurveyDissatisfactionReason.SWIMMING_POOL_INACCURATE)
      );
    }

    private static CreateSurveyDissatisfactionResponseRequest createWithShortFeedback() {
      return new CreateSurveyDissatisfactionResponseRequest(
          "짧음",
          List.of(SurveyDissatisfactionReason.SWIMMING_POOL_INACCURATE)
      );
    }

    private static CreateSurveyDissatisfactionResponseRequest createWithLongFeedback() {
      val longFeedback = "a".repeat(501);
      return new CreateSurveyDissatisfactionResponseRequest(
          longFeedback,
          List.of(SurveyDissatisfactionReason.SWIMMING_POOL_INACCURATE)
      );
    }

    private static CreateSurveyDissatisfactionResponseRequest createWithNullReasons() {
      return new CreateSurveyDissatisfactionResponseRequest("앱이 사용하기 어려워서 개선이 필요합니다", null);
    }

    private static CreateSurveyDissatisfactionResponseRequest createWithEmptyReasons() {
      return new CreateSurveyDissatisfactionResponseRequest("앱이 사용하기 어려워서 개선이 필요합니다", List.of());
    }

    private static CreateSurveyDissatisfactionResponseRequest createWithMultipleReasons() {
      return new CreateSurveyDissatisfactionResponseRequest(
          "여러 가지 문제점이 있어서 개선이 필요합니다",
          List.of(
              SurveyDissatisfactionReason.SWIMMING_POOL_INACCURATE,
              SurveyDissatisfactionReason.PROGRAM_INFO_HARD_TO_SEE,
              SurveyDissatisfactionReason.OPERATION_COMPLEX
          )
      );
    }

  }

}
