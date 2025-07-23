package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingControllerTest.MEMBER_ID;
import static com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingControllerTest.SWIMMING_POOL_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.time.LocalDate;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolDetailFreeSwimmingController.class)
@WithMockTokenInfo(memberId = MEMBER_ID, swimmingPoolId = SWIMMING_POOL_ID)
class FindSwimmingPoolDetailFreeSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  static final long MEMBER_ID = 1L;
  static final long SWIMMING_POOL_ID = 1L;

  private static final String BASE_PATH = "/api/swimming-pools/{swimmingPoolId}/free-swimming";

  @Test
  @DisplayName("유효한 요청 시 200 OK를 반환한다")
  void shouldReturn200WhenValidRequest() throws Exception {
    // given
    val date = LocalDate.of(2025, 1, 15);

    // when
    // then
    mockMvc.perform(get(BASE_PATH, SWIMMING_POOL_ID)
            .param("date", date.toString()))
        .andExpect(status().isOk());
  }

  @Nested
  @DisplayName("요청 검증 테스트")
  class DescribeRequestValidation {

    @Test
    @DisplayName("날짜 파라미터가 누락된 경우 400 Bad Request를 반환한다")
    void shouldReturn400WhenDateParameterIsMissing() throws Exception {
      // given
      // whehn
      // then
      mockMvc.perform(get(BASE_PATH, SWIMMING_POOL_ID))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 날짜 형식으로 요청 시 400 Bad Request를 반환한다")
    void shouldReturn400WhenDateFormatIsInvalid() throws Exception {
      // given
      val invalidDate = "2025-13-01"; // 잘못된 월

      // when & then
      mockMvc.perform(get(BASE_PATH, SWIMMING_POOL_ID)
              .param("date", invalidDate))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("날짜가 빈 문자열인 경우 400 Bad Request를 반환한다")
    void shouldReturn400WhenDateIsEmpty() throws Exception {
      // given
      // when
      // then
      mockMvc.perform(get(BASE_PATH, SWIMMING_POOL_ID)
              .param("date", ""))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("수영장 ID가 0 이하인 경우 400 Bad Request를 반환한다")
    void shouldReturn400WhenSwimmingPoolIdIsZeroOrNegative() throws Exception {
      // given
      val date = LocalDate.of(2025, 1, 15);

      // when
      // then
      // 0인 경우
      mockMvc.perform(get(BASE_PATH, 0L)
              .param("date", date.toString()))
          .andExpect(status().isBadRequest());

      // 음수인 경우
      mockMvc.perform(get(BASE_PATH, -1L)
              .param("date", date.toString()))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유효한 수영장 ID(양수)로 요청 시 정상 처리한다")
    void shouldProcessValidSwimmingPoolId() throws Exception {
      // given
      val date = LocalDate.of(2025, 1, 15);

      // when
      // then
      mockMvc.perform(get(BASE_PATH, 999L)
              .param("date", date.toString()))
          .andExpect(status().isOk());
    }

  }

}
