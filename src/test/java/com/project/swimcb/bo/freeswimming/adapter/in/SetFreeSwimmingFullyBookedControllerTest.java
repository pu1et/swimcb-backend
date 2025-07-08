package com.project.swimcb.bo.freeswimming.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = SetFreeSwimmingFullyBookedController.class)
class SetFreeSwimmingFullyBookedControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private final String PATH = "/api/bo/free-swimming/fully-booked";

  @Test
  @DisplayName("유효한 요청이면 200 OK를 반환한다")
  void shouldReturn200WhenValidRequest() throws Exception {
    // given
    val request = createValidRequest();

    // when & then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Nested
  @DisplayName("요청 검증 시")
  class DescribeRequestValidation {

    @Test
    @DisplayName("freeSwimmingDayStatusIds가 null이면 400 에러를 반환한다")
    void shouldReturn400WhenFreeSwimmingDayStatusIdsIsNull() throws Exception {
      // given
      val request = SetFreeSwimmingFullyBookedRequest.builder()
          .freeSwimmingDayStatusIds(null)
          .isFullyBooked(true)
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
      val request = SetFreeSwimmingFullyBookedRequest.builder()
          .freeSwimmingDayStatusIds(List.of())
          .isFullyBooked(true)
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
    @DisplayName("isFullyBooked가 null이면 400 에러를 반환한다")
    void shouldReturn400WhenIsFullyBookedIsNull() throws Exception {
      // given
      val request = SetFreeSwimmingFullyBookedRequest.builder()
          .freeSwimmingDayStatusIds(List.of(1L, 2L))
          .isFullyBooked(null)
          .build();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("예약마감 여부는 null이 될 수 없습니다")));
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

  private SetFreeSwimmingFullyBookedRequest createValidRequest() {
    return SetFreeSwimmingFullyBookedRequest.builder()
        .freeSwimmingDayStatusIds(List.of(1L, 2L, 3L))
        .isFullyBooked(true)
        .build();
  }

}
