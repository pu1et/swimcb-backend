package com.project.swimcb.bo.freeswimming.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CancelBoFreeSwimmingController.class)
class CancelBoFreeSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private final String PATH = "/api/bo/free-swimming/1/cancel";

  @Test
  @DisplayName("자유수영 데이터 관리 - 폐강 성공")
  void shouldCancelBoFreeSwimming() throws Exception {
    // given
    val request = createValidRequest();
    // when
    // then
    mockMvc.perform(patch(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Nested
  @DisplayName("요청 검증 시")
  class DescribeRequestValidation {

    @Test
    @DisplayName("폐강 사유가 null인 경우 400 반환")
    void shouldReturn400WhenReasonIsNull() throws Exception {
      // given
      val request = new CancelBoFreeSwimmingRequest(null);
      // when
      // then
      mockMvc.perform(patch(PATH)
              .contentType(APPLICATION_JSON_VALUE)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("폐강 사유는 필수입니다")));
    }

  }

  private CancelBoFreeSwimmingRequest createValidRequest() {
    return new CancelBoFreeSwimmingRequest("DUMMY_REASON");
  }

}
