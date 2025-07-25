package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingDetailControllerTest.MEMBER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFreeSwimmingDetailUseCase;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.FreeSwimming;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.Ticket;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolDetailFreeSwimmingDetailController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindSwimmingPoolDetailFreeSwimmingDetailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindSwimmingPoolDetailFreeSwimmingDetailUseCase useCase;

  @MockitoBean
  private FindSwimmingPoolDetailFreeSwimmingDetailResponseMapper mapper;

  static final long MEMBER_ID = 1L;
  private static final long SWIMMING_POOL_ID = 1L;
  private static final String PATH = "/api/swimming-pools/{swimmingPoolId}/free-swimming/{date}";
  private static final LocalDate DATE = LocalDate.of(2025, 1, 15);

  @Test
  @DisplayName("수영장 상세 조회 - 자유수영 특정 날짜로 조회 성공")
  void shouldFindSwimmingPoolDetailFreeSwimmingDetailSuccessfully() throws Exception {
    // given
    val result = createSwimmingPoolDetailFreeSwimmingDetail();
    val response = createFindSwimmingPoolDetailFreeSwimmingDetailResponse();

    given(useCase.findSwimmingPoolDetailFreeSwimmingDetail(any())).willReturn(result);
    given(mapper.toResponse(result)).willReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH, SWIMMING_POOL_ID, DATE)
            .param("date", DATE.toString()))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    then(useCase).should().findSwimmingPoolDetailFreeSwimmingDetail(argThat(
        i -> i.swimmingPoolId().equals(SWIMMING_POOL_ID) && i.date().equals(DATE)));
    then(mapper).should().toResponse(result);
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
  @DisplayName("date 파라미터가 잘못된 경우 400 반환")
  void shouldReturn400WhenDateParameterIsMissing() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get(PATH, SWIMMING_POOL_ID, "2025-01"))
        .andExpect(status().isBadRequest());
  }

  private SwimmingPoolDetailFreeSwimmingDetail createSwimmingPoolDetailFreeSwimmingDetail() {
    val time = Time.builder()
        .startTime(LocalTime.of(9, 0))
        .endTime(LocalTime.of(10, 0))
        .build();

    val ticket = Ticket.builder()
        .id(1L)
        .name("성인권")
        .price(5000)
        .build();

    val freeSwimming = FreeSwimming.builder()
        .dayStatusId(100L)
        .time(time)
        .minTicketPrice(3000)
        .tickets(List.of(ticket))
        .favoriteId(200L)
        .build();

    return new SwimmingPoolDetailFreeSwimmingDetail(List.of(freeSwimming));
  }

  private FindSwimmingPoolDetailFreeSwimmingDetailResponse createFindSwimmingPoolDetailFreeSwimmingDetailResponse() {
    val time = FindSwimmingPoolDetailFreeSwimmingDetailResponse.Time.builder()
        .startTime(LocalTime.of(9, 0))
        .endTime(LocalTime.of(10, 0))
        .build();

    val ticket = FindSwimmingPoolDetailFreeSwimmingDetailResponse.Ticket.builder()
        .id(1L)
        .name("성인권")
        .price(5000)
        .build();

    val freeSwimming = FindSwimmingPoolDetailFreeSwimmingDetailResponse.FreeSwimming.builder()
        .dayStatusId(100L)
        .time(time)
        .minTicketPrice(3000)
        .tickets(List.of(ticket))
        .favoriteId(200L)
        .build();
    return new FindSwimmingPoolDetailFreeSwimmingDetailResponse(List.of(freeSwimming));
  }

}
