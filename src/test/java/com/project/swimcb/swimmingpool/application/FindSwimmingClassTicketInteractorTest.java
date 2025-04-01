package com.project.swimcb.swimmingpool.application;

import static com.project.swimcb.swimmingpool.domain.SwimmingClassReservationStatus.RESERVABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingClassTicketGateway;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo.SwimmingClass;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingClassTicketInteractorTest {

  @InjectMocks
  private FindSwimmingClassTicketInteractor interactor;

  @Mock
  private FindSwimmingClassTicketGateway gateway;

  @Test
  @DisplayName("수영장 티켓 정보를 성공적으로 조회한다.")
  void shouldFindSwimmingClassTicketSuccessfully() {
    // given
    val ticketId = 1L;
    val expectedTicketInfo = TestSwimmingClassTicketInfoFactory.create();

    when(gateway.findSwimmingClassTicket(anyLong())).thenReturn(expectedTicketInfo);
    // when
    val actualTicketInfo = interactor.findSwimmingClassTicket(ticketId);

    // then
    assertThat(actualTicketInfo).isEqualTo(expectedTicketInfo);

    verify(gateway).findSwimmingClassTicket(ticketId);
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