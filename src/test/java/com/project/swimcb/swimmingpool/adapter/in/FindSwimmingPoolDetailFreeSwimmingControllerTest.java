package com.project.swimcb.swimmingpool.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFreeSwimmingUseCase;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
import java.time.YearMonth;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolDetailFreeSwimmingController.class)
class FindSwimmingPoolDetailFreeSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindSwimmingPoolDetailFreeSwimmingUseCase useCase;

  @MockitoBean
  private FindSwimmingPoolDetailFreeSwimmingResponseMapper mapper;

  private static final long SWIMMING_POOL_ID = 1L;
  private static final String PATH = "/api/swimming-pools/{swimmingPoolId}/free-swimming";

  @Test
  @DisplayName("수영장 상세 조회 - 자유수영 스케쥴 조회 성공")
  void shouldFindSwimmingPoolDetailFreeSwimmingSuccessfully() throws Exception {
    // given
    val month = YearMonth.of(2025, 1);

    val result = new SwimmingPoolDetailFreeSwimming(List.of());
    given(useCase.findSwimmingPoolDetailFreeSwimming(any())).willReturn(result);
    // when
    // then
    mockMvc.perform(get(PATH, SWIMMING_POOL_ID)
            .param("month", month.toString()))
        .andExpect(status().isOk());

    then(useCase).should().findSwimmingPoolDetailFreeSwimming(argThat(
        i -> i.swimmingPoolId().equals(SWIMMING_POOL_ID) && i.month().equals(month)));
    then(mapper).should().toResponse(result);
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
