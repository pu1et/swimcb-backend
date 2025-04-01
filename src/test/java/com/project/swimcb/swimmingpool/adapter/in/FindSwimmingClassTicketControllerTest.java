package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.domain.SwimmingClassReservationStatus.RESERVABLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingClassTicketUseCase;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo.SwimmingClass;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingClassTicketController.class)
class FindSwimmingClassTicketControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindSwimmingClassTicketUseCase useCase;

  @MockitoBean
  private FindSwimmingClassTicketResponseMapper mapper;

  private static final String PATH = "/api/swimming-pools/classes/tickets/{ticketId}";
  private static final long TICKET_ID = 1L;

  @Test
  @DisplayName("수영 상세 - 티켓 정보 조회 요청을 성공적으로 처리한다.")
  void shouldReturnResponseWhenSwimmingClassTicketExists() throws Exception {
    // given
    val result = TestSwimmingClassTicketInfoFactory.create();
    val expectedResponse = TestSwimmingClassTicketResponseFactory.create();

    when(useCase.findSwimmingClassTicket(anyLong())).thenReturn(result);
    when(mapper.toResponse(any())).thenReturn(expectedResponse);
    // when
    // then
    mockMvc.perform(get(PATH, TICKET_ID))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

    verify(useCase, only()).findSwimmingClassTicket(TICKET_ID);
    verify(mapper, only()).toResponse(result);
  }

  private static class TestSwimmingClassTicketInfoFactory {

    private static SwimmingClassTicketInfo create() {
      return SwimmingClassTicketInfo.builder()
          .swimmingClass(
              SwimmingClass.builder()
                  .id(1L)
                  .type("DUMMY_TYPE")
                  .subType("DUMMY_SUB_TYPE")
                  .days(List.of("월", "화", "수"))
                  .startTime(LocalTime.of(10, 0))
                  .endTime(LocalTime.of(11, 0))
                  .build()
          )
          .ticket(
              SwimmingClassTicketInfo.SwimmingClassTicket.builder()
                  .name("DUMMY_TICKET_NAME")
                  .price(10000)
                  .status(RESERVABLE)
                  .build()
          )
          .build();
    }
  }

  private static class TestSwimmingClassTicketResponseFactory {

    private static FindSwimmingClassTicketResponse create() {
      return FindSwimmingClassTicketResponse.builder()
          .swimmingClass(
              FindSwimmingClassTicketResponse.SwimmingClass.builder()
                  .type("DUMMY_TYPE")
                  .subType("DUMMY_SUB_TYPE")
                  .days(List.of("월", "화", "수"))
                  .startTime(LocalTime.of(10, 0))
                  .endTime(LocalTime.of(11, 0))
                  .build()
          )
          .ticket(
              FindSwimmingClassTicketResponse.SwimmingClassTicket.builder()
                  .name("DUMMY_TICKET_NAME")
                  .price(10000)
                  .status(RESERVABLE)
                  .build()
          )
          .build();
    }
  }
}