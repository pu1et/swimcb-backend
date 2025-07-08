package com.project.swimcb.bo.freeswimming.adapter.in;

import static com.project.swimcb.bo.freeswimming.adapter.in.SetFreeSwimmingHolidayControllerTest.SWIMMING_POOL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.freeswimming.application.port.in.SetFreeSwimmingClosedUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = SetFreeSwimmingHolidayController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class SetFreeSwimmingHolidayControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private SetFreeSwimmingClosedUseCase useCase;

  static final long SWIMMING_POOL_ID = 1L;

  private final String PATH = "/api/bo/free-swimming/holidays";

  @Test
  @DisplayName("유효한 요청이면 200 OK를 반환한다")
  void shouldReturn200WhenValidRequest() throws Exception {
    // given
    val request = createValidRequest();

    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    then(useCase).should().setFreeSwimmingClosed(assertArg(i -> {
      assertThat(i.swimmingPoolId()).isEqualTo(SWIMMING_POOL_ID);
      assertThat(i.freeSwimmingDayStatusIds()).isEqualTo(request.freeSwimmingDayStatusIds());
      assertThat(i.isClosed()).isEqualTo(request.isHoliday());
    }));
  }

  @Nested
  @DisplayName("요청 검증 시")
  class DescribeRequestValidation {

    @Test
    @DisplayName("freeSwimmingDayStatusIds가 null이면 400 에러를 반환한다")
    void shouldReturn400WhenFreeSwimmingDayStatusIdsIsNull() throws Exception {
      // given
      val request = SetFreeSwimmingHolidayRequest.builder()
          .freeSwimmingDayStatusIds(null)
          .isHoliday(true)
          .build();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("자유수영 일별 상태 ID 리스트는 필수입니다")));
    }

    @Test
    @DisplayName("freeSwimmingDayStatusIds가 빈 리스트이면 400 에러를 반환한다")
    void shouldReturn400WhenFreeSwimmingDayStatusIdsIsEmpty() throws Exception {
      // given
      val request = SetFreeSwimmingHolidayRequest.builder()
          .freeSwimmingDayStatusIds(List.of())
          .isHoliday(true)
          .build();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("자유수영 일별 상태 ID 리스트는 필수입니다")));
    }

    @Test
    @DisplayName("isHoliday가 null이면 400 에러를 반환한다")
    void shouldReturn400WhenIsHolidayIsNull() throws Exception {
      // given
      val request = SetFreeSwimmingHolidayRequest.builder()
          .freeSwimmingDayStatusIds(List.of(1L, 2L))
          .isHoliday(null)
          .build();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("휴무일 여부는 null이 될 수 없습니다")));
    }

    @Test
    @DisplayName("Content-Type이 없으면 415 에러를 반환한다")
    void shouldReturn415WhenContentTypeIsMissing() throws Exception {
      // given
      val request = createValidRequest();

      // when & then
      mockMvc.perform(post(PATH)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("잘못된 JSON 형식이면 400 에러를 반환한다")
    void shouldReturn400WhenJsonIsMalformed() throws Exception {
      // when & then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content("{invalid json}"))
          .andExpect(status().isBadRequest());
    }

  }

  private SetFreeSwimmingHolidayRequest createValidRequest() {
    return SetFreeSwimmingHolidayRequest.builder()
        .freeSwimmingDayStatusIds(List.of(1L, 2L, 3L))
        .isHoliday(true)
        .build();
  }

}
