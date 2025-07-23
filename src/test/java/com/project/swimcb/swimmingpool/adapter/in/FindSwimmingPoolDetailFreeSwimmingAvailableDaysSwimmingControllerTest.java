package com.project.swimcb.swimmingpool.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFreeSwimmingAvailableDaysUseCase;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeAvailableDays;
import java.time.YearMonth;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolDetailFreeSwimmingAvailableDaysSwimmingController.class)
class FindSwimmingPoolDetailFreeSwimmingAvailableDaysSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindSwimmingPoolDetailFreeSwimmingAvailableDaysUseCase useCase;

  private static final long SWIMMING_POOL_ID = 1L;
  private static final String PATH = "/api/swimming-pools/{swimmingPoolId}/free-swimming/available-days";

  @Test
  @DisplayName("수영장 상세 조회 - 자유수영 가능 날짜 조회 성공")
  void shouldFindSwimmingPoolDetailFreeSwimmingAvailableDaysSuccessfully() throws Exception {
    // given
    val month = YearMonth.of(2025, 1);

    val result = new SwimmingPoolDetailFreeAvailableDays(List.of(1, 2, 3));
    given(useCase.findSwimmingPoolDetailFreeSwimmingAvailableDays(any())).willReturn(result);

    val response = new FindSwimmingPoolDetailFreeSwimmingAvailableDaysResponse(result.days());

    // when
    // then
    mockMvc.perform(get(PATH, SWIMMING_POOL_ID)
            .param("month", month.toString()))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    then(useCase).should().findSwimmingPoolDetailFreeSwimmingAvailableDays(argThat(
        i -> i.swimmingPoolId().equals(SWIMMING_POOL_ID) && i.month().equals(month)));

  }

  @Test
  @DisplayName("swimmingPoolId가 0 이하일 경우 400 반환")
  void shouldReturn400WhenSwimmingPoolIdIsZeroOrNegative() throws Exception {
    // given
    val invalidSwimmingPoolId = 0L;
    val month = YearMonth.of(2025, 1);

    // when
    // then
    mockMvc.perform(get(PATH, invalidSwimmingPoolId)
            .param("month", month.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("month 파라미터가 누락된 경우 400 반환")
  void shouldReturn400WhenMonthParameterIsMissing() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get(PATH, SWIMMING_POOL_ID))
        .andExpect(status().isBadRequest());
  }

}
