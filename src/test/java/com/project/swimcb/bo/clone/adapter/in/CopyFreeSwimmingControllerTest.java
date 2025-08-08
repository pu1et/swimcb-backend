package com.project.swimcb.bo.clone.adapter.in;

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
import com.project.swimcb.bo.clone.application.port.in.CopyFreeSwimmingUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.time.YearMonth;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CopyFreeSwimmingController.class)
@WithMockTokenInfo(swimmingPoolId = CopyFreeSwimmingControllerTest.SWIMMING_POOL_ID)
class CopyFreeSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CopyFreeSwimmingUseCase useCase;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/free-swimming/copy";

  @Nested
  @DisplayName("자유수영 복사 API는")
  class CopyFreeSwimmingApi {

    @Test
    @DisplayName("자유수영 복사를 성공적으로 수행한다")
    void shouldCopyFreeSwimmingSuccessfully() throws Exception {
      // given
      val request = TestCopyFreeSwimmingRequestFactory.create();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());

      then(useCase).should(only()).copyFreeSwimming(assertArg(command -> {
        assertThat(command.fromMonth()).isEqualTo(request.fromMonth());
        assertThat(command.toMonth()).isEqualTo(request.toMonth());
      }));
    }

    @Test
    @DisplayName("fromMonth가 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenFromMonthIsNull() throws Exception {
      // given
      val request = TestCopyFreeSwimmingRequestFactory.createWithNullFromMonth();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("fromMonth는 null일 수 없습니다")));
    }

    @Test
    @DisplayName("toMonth가 null인 경우 400 에러를 반환한다")
    void shouldReturn400WhenToMonthIsNull() throws Exception {
      // given
      val request = TestCopyFreeSwimmingRequestFactory.createWithNullToMonth();

      // when
      // then
      mockMvc.perform(post(PATH)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("toMonth는 null일 수 없습니다")));
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

  }

  private static class TestCopyFreeSwimmingRequestFactory {

    private static CopyFreeSwimmingRequest create() {
      return new CopyFreeSwimmingRequest(
          YearMonth.of(2025, 1),
          YearMonth.of(2025, 2)
      );
    }

    private static CopyFreeSwimmingRequest createWithNullFromMonth() {
      return new CopyFreeSwimmingRequest(
          null,
          YearMonth.of(2025, 2)
      );
    }

    private static CopyFreeSwimmingRequest createWithNullToMonth() {
      return new CopyFreeSwimmingRequest(
          YearMonth.of(2025, 1),
          null
      );
    }

  }

}
