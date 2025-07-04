package com.project.swimcb.bo.freeswimming.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.freeswimming.adapter.out.FindBoFreeSwimmingResponseMapper;
import com.project.swimcb.bo.freeswimming.application.port.in.FindBoFreeSwimmingUseCase;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindBoFreeSwimmingController.class)
@WithMockTokenInfo(swimmingPoolId = FindBoFreeSwimmingControllerTest.SWIMMING_POOL_ID)
class FindBoFreeSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindBoFreeSwimmingUseCase useCase;

  @MockitoBean
  private FindBoFreeSwimmingResponseMapper mapper;

  @Autowired
  private ObjectMapper objectMapper;

  static final long SWIMMING_POOL_ID = 1L;
  private static final String PATH = "/api/bo/free-swimming";

  @Nested
  @DisplayName("자유수영 데이터 조회 시")
  class DescribeFindBoFreeSwimming {

    @Nested
    @DisplayName("해당 월에 자유수영 데이터가 존재하는 경우")
    class ContextWithExistingFreeSwimmingData {

      @Test
      @DisplayName("자유수영 목록을 정상 반환한다")
      void shouldReturnFreeSwimmingList() throws Exception {
        // given
        val freeSwimmings = List.of(TestBoFreeSwimmingFactory.create());
        val response = TestFindBoFreeSwimmingResponseFactory.create();

        when(useCase.findBoFreeSwimming(SWIMMING_POOL_ID, LocalDate.of(2025, 1, 1)))
            .thenReturn(freeSwimmings);
        when(mapper.toResponse(freeSwimmings)).thenReturn(response);

        // when
        // then
        mockMvc.perform(get(PATH)
                .param("yearMonth", "2025-01"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
      }

    }

    @Nested
    @DisplayName("해당 월에 자유수영 데이터가 없는 경우")
    class ContextWithNoFreeSwimmingData {

      @Test
      @DisplayName("빈 목록을 반환한다")
      void shouldReturnEmptyList() throws Exception {
        // given
        val emptyResponse = new FindBoFreeSwimmingResponse(List.of());

        when(useCase.findBoFreeSwimming(any(Long.class), any(LocalDate.class)))
            .thenReturn(List.of());
        when(mapper.toResponse(eq(List.of()))).thenReturn(emptyResponse);

        // when & then
        mockMvc.perform(get(PATH)
                .param("yearMonth", "2025-02"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(emptyResponse)));
      }

    }

    @Nested
    @DisplayName("잘못된 요청 파라미터의 경우")
    class ContextWithInvalidParameters {

      @Test
      @DisplayName("yearMonth 파라미터가 없으면 400 에러를 반환한다")
      void shouldReturn400WhenYearMonthMissing() throws Exception {
        // when
        // then
        mockMvc.perform(get(PATH))
            .andExpect(status().isBadRequest());
      }

      @Test
      @DisplayName("잘못된 형식의 yearMonth이면 400 에러를 반환한다")
      void shouldReturn400WhenYearMonthInvalid() throws Exception {
        // when
        // then
        mockMvc.perform(get(PATH)
                .param("yearMonth", "invalid-format"))
            .andExpect(status().isBadRequest());
      }

    }

  }

  private static class TestBoFreeSwimmingFactory {

    private static BoFreeSwimming create() {
      return BoFreeSwimming.builder()
          .freeSwimmingId(1L)
          .days(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
          .time(new BoFreeSwimming.Time(LocalTime.of(6, 0), LocalTime.of(6, 50)))
          .lifeguard(new BoFreeSwimming.Lifeguard(1L, "김강사"))
          .tickets(List.of(new BoFreeSwimming.Ticket(1L, "성인일반", 10000)))
          .ticketPriceRange(new BoFreeSwimming.TicketPriceRange(10000, 10000))
          .capacity(20)
          .isExposed(true)
          .build();
    }

  }

  private static class TestFindBoFreeSwimmingResponseFactory {

    private static FindBoFreeSwimmingResponse create() {
      return new FindBoFreeSwimmingResponse(
          List.of(
              FindBoFreeSwimmingResponse.FreeSwimming.builder()
                  .freeSwimmingId(1L)
                  .days(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
                  .time(
                      new FindBoFreeSwimmingResponse.Time(LocalTime.of(6, 0), LocalTime.of(6, 50)))
                  .lifeguard(new FindBoFreeSwimmingResponse.Lifeguard(1L, "김강사"))
                  .ticketPriceRange(new FindBoFreeSwimmingResponse.TicketPriceRange(10000, 10000))
                  .tickets(List.of(
                      new FindBoFreeSwimmingResponse.Ticket(1L, "성인일반", 10000)
                  ))
                  .capacity(20)
                  .isExposed(true)
                  .build()
          ));
    }

  }

}
