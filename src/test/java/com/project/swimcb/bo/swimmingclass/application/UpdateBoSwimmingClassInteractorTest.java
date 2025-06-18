package com.project.swimcb.bo.swimmingclass.application;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.db.entity.SwimmingClassEntity;
import com.project.swimcb.db.repository.SwimmingClassRepository;
import com.project.swimcb.db.entity.SwimmingClassTicketEntity;
import com.project.swimcb.db.repository.SwimmingClassTicketRepository;
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

  @Mock
  private BoCancelReservationDsGateway boCancelReservationDsGateway;

  @Nested
  @DisplayName("수영 클래스가 존재하는 경우")
  class WhenSwimmingClassExists {

    @Test
    @DisplayName("클래스 정보를 업데이트하고 기존 티켓을 전부 삭제 후 새 티켓을 생성한다.")
    void shouldUpdateSwimmingClassSuccessfully() {
      // given
      val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(5, 3);
      val swimmingClass = TestSwimmingClassFactory.createWithReservedCount(1);

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
        assertThat(i).extracting(SwimmingClassTicketEntity::getName)
            .containsExactly("DUMMY_TICKET_NAME1", "DUMMY_TICKET_NAME2");
        assertThat(i).extracting(SwimmingClassTicketEntity::getPrice)
            .containsExactly(10000, 20000);
      }));
    }

  }

  @Nested
  @DisplayName("예약 제한 유효성 검증")
  class ReservationLimitValidation {

    @Nested
    @DisplayName("대기 예약과 상관없는 공통 케이스")
    class CommonCases {

      @Nested
      @DisplayName("통과 케이스")
      class SuccessCases {

        @Test
        @DisplayName("예약 허용 인원수는 정원보다 같거나 적어야 한다.")
        void shouldThrowWhenReservationLimitCountGreaterThanTotalCapacity() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 5);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(5, 5);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertDoesNotThrow(() -> interactor.updateBoSwimmingClass(request));
        }

        @Test
        @DisplayName("예약 허용 인원 수는 0명 이상이어야 한다.")
        void shouldThrowWhenReservationLimitCountLessThanZero() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(0, 0);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(1, 0);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertDoesNotThrow(() -> interactor.updateBoSwimmingClass(request));
        }
      }

      @Nested
      @DisplayName("예외 케이스")
      class FailureCases {

        @Test
        @DisplayName("예약 허용 인원이 정원보다 많으면 예외가 발생한다")
        void shouldThrowWhenReservationLimitGreaterThanTotalCapacity() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 5);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(5, 6);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertThatThrownBy(() -> interactor.updateBoSwimmingClass(request))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("예약 허용 인원은 정원보다 같거나 적어야 합니다.");
        }

        @Test
        @DisplayName("예약 허용 인원이 0보다 작으면 예외가 발생한다")
        void shouldThrowWhenReservationLimitLessThanZero() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(-1, -1);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(10, -1);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertThatThrownBy(() -> interactor.updateBoSwimmingClass(request))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("예약 허용 인원은 0명 이상이어야 합니다.");
        }
      }

    }


    @Nested
    @DisplayName("대기 예약이 없는 경우")
    class WhenWaitingReservationsDoesNotExist {

      @Nested
      @DisplayName("통과 케이스")
      class SuccessCases {

        @Test
        @DisplayName("정원은 현재 예약된 인원보다 같거나 많아야 한다.")
        void shouldThrowWhenTotalCapacityLessThanOrEqualToReservedCount() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 4);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(4, 4);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertDoesNotThrow(() -> interactor.updateBoSwimmingClass(request));
        }

        @Test
        @DisplayName("예약 허용 인원수는 현재 예약된 인원보다 같거나 많아야 한다.")
        void shouldThrowWhenReservationLimitCountLessThanOrEqualToReservedCount() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 4);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(7, 4);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertDoesNotThrow(() -> interactor.updateBoSwimmingClass(request));
        }

      }

      @Nested
      @DisplayName("예외 케이스")
      class FailureCases {

        @Test
        @DisplayName("정원이 현재 예약 인원보다 적으면 예외가 발생한다")
        void shouldThrowWhenTotalCapacityLessThanReservedCount() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 4);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(3, 2);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertThatThrownBy(() -> interactor.updateBoSwimmingClass(request))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("정원은 현재 예약된 인원보다 많아야 합니다.");
        }

        @Test
        @DisplayName("예약 허용 인원이 현재 예약 인원보다 적으면 예외가 발생한다")
        void shouldThrowWhenReservationLimitCountLessThanReservedCount() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 4);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(5, 3);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertThatThrownBy(() -> interactor.updateBoSwimmingClass(request))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("예약 허용 인원 수는 현재 예약 인원보다 많아야 합니다.");
        }

      }

    }

    @Nested
    @DisplayName("대기 예약이 있는 경우")
    class WhenWaitingReservationsExist {

      @Nested
      @DisplayName("통과 케이스")
      class SuccessCases {

        @Test
        @DisplayName("정원은 현재 예약 허용 인원보다 같거나 많아야 한다.")
        void shouldThrowWhenTotalCapacityLessThanOrEqualToReservedCount() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 7);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(5, 5);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertDoesNotThrow(() -> interactor.updateBoSwimmingClass(request));
        }

        @Test
        @DisplayName("예약 허용 인원수는 현재 예약 허용 인원보다 같거나 많아야 한다.")
        void shouldThrowWhenReservationLimitCountLessThanOrEqualToReservedCount() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 7);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(7, 5);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertDoesNotThrow(() -> interactor.updateBoSwimmingClass(request));
        }

      }

      @Nested
      @DisplayName("예외 케이스")
      class FailureCases {

        @Test
        @DisplayName("정원이 현재 예약 허용 인원보다 적으면 예외가 발생한다")
        void shouldThrowWhenTotalCapacityLessThanReservedCount() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 7);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(4, 3);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertThatThrownBy(() -> interactor.updateBoSwimmingClass(request))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("정원은 현재 예약 허용 인원보다 많아야 합니다.");
        }

        @Test
        @DisplayName("예약 허용 인원이 현재 예약 허용 인원보다 적으면 예외가 발생한다")
        void shouldThrowWhenReservationLimitCountLessThanReservedCount() {
          // given
          val swimmingClass = TestSwimmingClassFactory.create(5, 7);
          val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(10, 4);

          when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
              .thenReturn(Optional.of(swimmingClass));

          // when
          // then
          assertThatThrownBy(() -> interactor.updateBoSwimmingClass(request))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("예약 허용 인원 수는 현재 예약 허용 인원보다 많아야 합니다.");
        }

      }

    }

  }

  @Nested
  @DisplayName("예약 허용 인원수 증가시 대기열 처리")
  class WhenReservationLimitIncreases {

    @Test
    @DisplayName("예약 허용 인원수가 증가하지 않으면 대기열 처리가 발생하지 않는다")
    void shouldNotAdvanceWaitingReservationsWhenLimitNotIncreased() {
      // given
      val swimmingClass = TestSwimmingClassFactory.create(5, 3);
      val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(10, 5);

      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.of(swimmingClass));

      // when
      interactor.updateBoSwimmingClass(request);

      // then
      verify(boCancelReservationDsGateway, never())
          .findWaitingReservationIdsBySwimmingClassIdLimit(anyLong(), anyInt());
      verify(boCancelReservationDsGateway, never())
          .updateReservationStatusToPaymentPending(anyList());
    }

    @Test
    @DisplayName("예약 허용 인원수가 증가했지만 대기중인 예약이 없으면 상태 변경이 발생하지 않는다")
    void shouldNotUpdateStatusWhenNoWaitingReservations() {
      // given
      val swimmingClass = TestSwimmingClassFactory.create(5, 3);
      val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(10, 7);
      val increasedAmount = 2;

      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.of(swimmingClass));
      when(boCancelReservationDsGateway.findWaitingReservationIdsBySwimmingClassIdLimit(
          swimmingClass.getId(), increasedAmount))
          .thenReturn(List.of());

      // when
      interactor.updateBoSwimmingClass(request);

      // then
      verify(boCancelReservationDsGateway, times(1))
          .findWaitingReservationIdsBySwimmingClassIdLimit(swimmingClass.getId(), increasedAmount);
      verify(boCancelReservationDsGateway, never())
          .updateReservationStatusToPaymentPending(anyList());
    }

    @Test
    @DisplayName("예약 허용 인원수가 증가하고 대기중인 예약이 있으면 증가한 인원수만큼 해당 예약을 결제대기 상태로 변경한다")
    void shouldAdvanceWaitingReservationsWhenLimitIncreased() {
      // given
      val swimmingClass = TestSwimmingClassFactory.create(5, 8);
      val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(10, 7);
      val increasedAmount = 2;
      val waitingReservationIds = List.of(101L, 102L);

      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.of(swimmingClass));
      when(boCancelReservationDsGateway.findWaitingReservationIdsBySwimmingClassIdLimit(
          swimmingClass.getId(), increasedAmount))
          .thenReturn(waitingReservationIds);

      // when
      interactor.updateBoSwimmingClass(request);

      // then
      verify(boCancelReservationDsGateway, times(1))
          .findWaitingReservationIdsBySwimmingClassIdLimit(swimmingClass.getId(), increasedAmount);
      verify(boCancelReservationDsGateway, times(1))
          .updateReservationStatusToPaymentPending(waitingReservationIds);
    }

  }

  @Nested
  @DisplayName("수영 클래스가 존재하지 않는 경우")
  class WhenSwimmingClassDoesNotExist {

    @Test
    @DisplayName("NoSuchElementException가 발생한다.")
    void shouldThrowNoSuchElementExceptionWhenSwimmingClassNotFound() {
      // given
      val request = TestUpdateBoSwimmingClassCommandFactory.createWithCapacity(5, 3);

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

    private static UpdateBoSwimmingClassCommand createWithCapacity(int totalCapacity,
        int reservationLimitCount) {
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
              .totalCapacity(totalCapacity)
              .reservationLimitCount(reservationLimitCount)
              .build())
          .isExposed(true)
          .build();
    }

  }


  private static class TestSwimmingClassFactory {

    private static SwimmingClassEntity common() {
      val swimmingClass = mock(SwimmingClassEntity.class);
      lenient().when(swimmingClass.getId()).thenReturn(2L);
      return swimmingClass;
    }

    private static SwimmingClassEntity create(int reservationLimitCount, int reservedCount) {
      val swimmingClass = common();
      when(swimmingClass.getReservedCount()).thenReturn(reservedCount);
      when(swimmingClass.getReservationLimitCount()).thenReturn(reservationLimitCount);
      return swimmingClass;
    }

    private static SwimmingClassEntity createWithReservedCount(int reservedCount) {
      val swimmingClass = common();
      when(swimmingClass.getReservedCount()).thenReturn(reservedCount);
      return swimmingClass;
    }

  }

}
