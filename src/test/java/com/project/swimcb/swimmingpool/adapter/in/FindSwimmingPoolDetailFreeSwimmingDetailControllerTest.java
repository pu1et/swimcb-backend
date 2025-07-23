package com.project.swimcb.swimmingpool.adapter.in;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import java.time.LocalDate;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolDetailFreeSwimmingDetailController.class)
class FindSwimmingPoolDetailFreeSwimmingDetailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private static final long SWIMMING_POOL_ID = 1L;
  private static final String PATH = "/api/swimming-pools/{swimmingPoolId}/free-swimming/{date}";
  private static final LocalDate DATE = LocalDate.of(2025, 1, 15);

  @Test
  @DisplayName("수영장 상세 조회 - 자유수영 특정 날짜로 조회 성공")
  void shouldFindSwimmingPoolDetailFreeSwimmingDetailSuccessfully() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get(PATH, SWIMMING_POOL_ID, DATE)
            .param("date", DATE.toString()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("swimmingPoolId가 0 이하일 경우 400 반환")
  void shouldReturn400WhenSwimmingPoolIdIsZeroOrNegative() throws Exception {
    // given
    val invalidSwimmingPoolId = 0L;
    // when
    // then
    mockMvc.perform(get(PATH, invalidSwimmingPoolId, DATE)
            .param("date", DATE.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("date 파라미터가 누락된 경우 400 반환")
  void shouldReturn400WhenDateParameterIsMissing() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get(PATH, SWIMMING_POOL_ID, DATE))
        .andExpect(status().isBadRequest());
  }

}
