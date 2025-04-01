package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.domain.SwimmingClassTicketReservationStatus.RESERVABLE;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo.SwimmingClass;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingClassTicketResponseFormatterTest {

  @InjectMocks
  private FindSwimmingClassTicketResponseFormatter formatter;

  @Test
  @DisplayName("SwimmingClassTicketInfo를 SwimmingClassTicketResponse로 올바르게 변환한다.")
  void shouldFormatSwimmingClassTicketInfoToResponse() {
    // given
    val ticketInfo = TestSwimmingClassTicketInfoFactory.create();
    // when
    val response = formatter.toResponse(ticketInfo);
    // then
    assertThat(response.swimmingClass().type()).isEqualTo(ticketInfo.swimmingClass().type());
    assertThat(response.swimmingClass().subType()).isEqualTo(ticketInfo.swimmingClass().subType());
    assertThat(response.swimmingClass().days()).isEqualTo(ticketInfo.swimmingClass().days());
    assertThat(response.swimmingClass().startTime()).isEqualTo(
        ticketInfo.swimmingClass().startTime());
    assertThat(response.swimmingClass().endTime()).isEqualTo(ticketInfo.swimmingClass().endTime());

    assertThat(response.ticket().name()).isEqualTo(ticketInfo.ticket().name());
    assertThat(response.ticket().price()).isEqualTo(ticketInfo.ticket().price());
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
}