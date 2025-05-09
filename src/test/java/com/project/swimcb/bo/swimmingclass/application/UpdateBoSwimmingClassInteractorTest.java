package com.project.swimcb.bo.swimmingclass.application;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicket;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicketRepository;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Ticket;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Time;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Type;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UpdateBoSwimmingClassInteractorTest {

  @InjectMocks
  private UpdateBoSwimmingClassInteractor interactor;

  @Mock
  private UpdateBoSwimmingClassDsGateway gateway;

  @Mock
  private SwimmingClassRepository swimmingClassRepository;

  @Mock
  private SwimmingClassTicketRepository ticketRepository;

  @Nested
  @DisplayName("수영 클래스가 존재하는 경우")
  class WhenSwimmingClassExists {

    @Test
    @DisplayName("클래스 정보를 업데이트하고 기존 티켓을 전부 삭제 후 새 티켓을 생성한다.")
    void shouldUpdateSwimmingClassSuccessfully() throws Exception {
      // given
      val request = TestUpdateBoSwimmingClassCommandFactory.create();
      val swimmingClass = TestSwimmingClassFactory.create();

      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.of(swimmingClass));
      // when
      interactor.updateBoSwimmingClass(request);
      // then
      verify(gateway, times(1)).updateSwimmingClass(request);
      verify(swimmingClassRepository, only()).findBySwimmingPool_IdAndId(request.swimmingPoolId(),
          request.swimmingClassId());
      verify(gateway, times(1)).deleteAllTicketsBySwimmingClassId(request.swimmingClassId());
      verify(ticketRepository, times(1)).saveAll(assertArg(i -> {
        assertThat(i).hasSize(2);
        assertThat(i).extracting(j -> j.getSwimmingClass().getId())
            .containsOnly(swimmingClass.getId());
        assertThat(i).extracting(SwimmingClassTicket::getName)
            .containsExactly("DUMMY_TICKET_NAME1", "DUMMY_TICKET_NAME2");
        assertThat(i).extracting(SwimmingClassTicket::getPrice)
            .containsExactly(10000, 20000);
      }));
    }

  }

  @Nested
  @DisplayName("수영 클래스가 존재하지 않는 경우")
  class WhenSwimmingClassDoesNotExist {

    @Test
    @DisplayName("NoSuchElementException가 발생한다.")
    void shouldThrowNoSuchElementExceptionWhenSwimmingClassNotFound() {
      // given
      val request = TestUpdateBoSwimmingClassCommandFactory.create();

      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.empty());
      // when
      // then
      assertThatThrownBy(() -> interactor.updateBoSwimmingClass(request))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessage("클래스가 존재하지 않습니다.");
    }

  }

  private static class TestUpdateBoSwimmingClassCommandFactory {

    private static UpdateBoSwimmingClassCommand create() {
      return UpdateBoSwimmingClassCommand.builder()
          .swimmingPoolId(1L)
          .swimmingClassId(2L)
          .type(Type.builder()
              .typeId(3L)
              .subTypeId(4L)
              .build())
          .days(List.of(MONDAY, WEDNESDAY, FRIDAY))
          .time(Time.builder()
              .startTime(LocalTime.of(9, 0))
              .endTime(LocalTime.of(10, 0))
              .build())
          .instructorId(5L)
          .tickets(List.of(
              Ticket.builder()
                  .name("DUMMY_TICKET_NAME1")
                  .price(10000)
                  .build(),
              Ticket.builder()
                  .name("DUMMY_TICKET_NAME2")
                  .price(20000)
                  .build()
          ))
          .registrationCapacity(RegistrationCapacity.builder()
              .totalCapacity(10)
              .reservationLimitCount(5)
              .build())
          .isExposed(true)
          .build();
    }

  }


  private static class TestSwimmingClassFactory {

    private static SwimmingClass create() throws Exception {
      val swimmingClassConstructor = SwimmingClass.class.getDeclaredConstructor();
      swimmingClassConstructor.setAccessible(true);
      val swimmingClass = swimmingClassConstructor.newInstance();
      ReflectionTestUtils.setField(swimmingClass, "id", 1L);
      return swimmingClass;
    }

  }

}
