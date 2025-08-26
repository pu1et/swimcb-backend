package com.project.swimcb.survey.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CreateSurveySatisfactionResponseController.class)
@WithMockTokenInfo(memberId = CreateSurveySatisfactionResponseControllerTest.MEMBER_ID)
class CreateSurveySatisfactionResponseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  static final long MEMBER_ID = 1L;

  private static final String PATH = "/api/surveys/satisfaction-responses";

  @Nested
  @DisplayName("만족도 평가 설문 응답 등록 API는")
  class CreateSurveySatisfactionResponseApi {

    @Test
    @DisplayName("만족도 평가 설문 응답을 성공적으로 등록한다")
    void shouldCreateSurveySatisfactionResponseSuccessfully() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.create();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("전체적인 만족도가 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenOverallRatingIsNull() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithNullOverallRating();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("전체적인 만족도는 null일 수 없습니다")));
    }

    @Test
    @DisplayName("전체적인 만족도가 1.0 미만인 경우 400 에러를 반환한다")
    void shouldReturn400WhenOverallRatingIsLessThanOne() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithInvalidOverallRatingMin();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("전체적인 만족도는 1.0 이상이어야 합니다")));
    }

    @Test
    @DisplayName("전체적인 만족도가 5.0 초과인 경우 400 에러를 반환한다")
    void shouldReturn400WhenOverallRatingIsGreaterThanFive() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithInvalidOverallRatingMax();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("전체적인 만족도는 5.0 이하여야 합니다")));
    }

    @Test
    @DisplayName("수영장 찾기 용이함 점수가 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFindPoolRatingIsNull() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithNullFindPoolRating();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("수영장 찾기 용이함 점수는 null일 수 없습니다")));
    }

    @Test
    @DisplayName("수영장 찾기 용이함 점수가 1 미만인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFindPoolRatingIsLessThanOne() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithInvalidFindPoolRatingMin();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("수영장 찾기 용이함 점수는 1 이상이어야 합니다")));
    }

    @Test
    @DisplayName("수영장 찾기 용이함 점수가 3 초과인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFindPoolRatingIsGreaterThanThree() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithInvalidFindPoolRatingMax();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("수영장 찾기 용이함 점수는 3 이하여야 합니다")));
    }

    @Test
    @DisplayName("예약 과정 간편 점수가 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenReservationRatingIsNull() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithNullReservationRating();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("예약 과정 간편 점수는 null일 수 없습니다")));
    }

    @Test
    @DisplayName("조작법 직관적 점수가 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenUsabilityRatingIsNull() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithNullUsabilityRating();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("조작법 직관적 점수는 null일 수 없습니다")));
    }

    @Test
    @DisplayName("추가 의견이 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFeedbackIsNull() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithNullFeedback();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("추가 의견은 null일 수 없습니다")));
    }

    @Test
    @DisplayName("추가 의견이 10자 미만인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFeedbackIsLessThanMinLength() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithTooShortFeedback();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("추가 의견은 10자 이상 500자 이하여야 합니다")));
    }

    @Test
    @DisplayName("추가 의견이 500자를 초과하는 경우 400 에러를 반환한다")
    void shouldReturn400WhenFeedbackExceedsMaxLength() throws Exception {
      // given
      val request = TestCreateSurveySatisfactionResponseRequestFactory.createWithTooLongFeedback();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("추가 의견은 10자 이상 500자 이하여야 합니다")));
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
      val request = TestCreateSurveySatisfactionResponseRequestFactory.create();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType("text/plain")
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isUnsupportedMediaType());
    }

  }

  private static class TestCreateSurveySatisfactionResponseRequestFactory {

    private static CreateSurveySatisfactionResponseRequest create() {
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          2,
          3,
          1,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithNullOverallRating() {
      return new CreateSurveySatisfactionResponseRequest(
          null,
          2,
          3,
          1,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithInvalidOverallRatingMin() {
      return new CreateSurveySatisfactionResponseRequest(
          0.5,
          2,
          3,
          1,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithInvalidOverallRatingMax() {
      return new CreateSurveySatisfactionResponseRequest(
          5.5,
          2,
          3,
          1,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithNullFindPoolRating() {
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          null,
          3,
          1,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithInvalidFindPoolRatingMin() {
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          0,
          3,
          1,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithInvalidFindPoolRatingMax() {
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          4,
          3,
          1,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithNullReservationRating() {
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          2,
          null,
          1,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithNullUsabilityRating() {
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          2,
          3,
          null,
          "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다."
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithNullFeedback() {
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          2,
          3,
          1,
          null
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithTooShortFeedback() {
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          2,
          3,
          1,
          "짧은 의견"  // 4자 (10자 미만)
      );
    }

    private static CreateSurveySatisfactionResponseRequest createWithTooLongFeedback() {
      // 501자 문자열 생성
      String longFeedback = "a".repeat(501);
      return new CreateSurveySatisfactionResponseRequest(
          4.5,
          2,
          3,
          1,
          longFeedback
      );
    }

  }

}
