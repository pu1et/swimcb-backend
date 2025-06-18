package com.project.swimcb.bo.swimmingclass.application;

import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_VERIFICATION;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.application.out.CancelBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.CancelBoSwimmingClassCommand;
import com.project.swimcb.db.entity.SwimmingClassEntity;
import com.project.swimcb.db.repository.SwimmingClassRepository;
import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancelBoSwimmingClassInteractorTest {

  @InjectMocks
  private CancelBoSwimmingClassInteractor interactor;

  @Mock
  private SwimmingClassRepository swimmingClassRepository;

  @Mock
  private UpdateBoSwimmingClassDsGateway updateBoSwimmingClassDsGateway;

  @Mock
  private CancelBoSwimmingClassDsGateway cancelBoSwimmingClassDsGateway;

  @Nested
  @DisplayName("수영 클래스 취소 시")
  class CancelSwimmingClassTests {

    private CancelBoSwimmingClassCommand command;

    @BeforeEach
    void setUp() {
      val swimmingPoolId = 1L;
      val swimmingClassId = 2L;
      val cancelReason = "DUMMY_CANCEL_REASON";
      command = CancelBoSwimmingClassCommand.builder()
          .swimmingPoolId(swimmingPoolId)
          .swimmingClassId(swimmingClassId)
          .cancelReason(cancelReason)
          .build();
    }

    @Test
    @DisplayName("클래스가 존재하고 결제완료/입금확인중인 예약이 없으면 클래스를 취소하고 예약을 취소 상태로 변경한다")
    void shouldCancelClassAndReservationsWhenNoCompletedPayments() {
      // given
      val swimmingClass = mock(SwimmingClassEntity.class);
      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.of(swimmingClass));
      when(cancelBoSwimmingClassDsGateway.existsReservationBySwimmingClassIdReservationStatusIn(
          any(), any())).thenReturn(false);

      // when
      interactor.cancelBoSwimmingClass(command);

      // then
      verify(cancelBoSwimmingClassDsGateway, times(1))
          .existsReservationBySwimmingClassIdReservationStatusIn(command.swimmingClassId(),
              List.of(PAYMENT_VERIFICATION, PAYMENT_COMPLETED));
      verify(updateBoSwimmingClassDsGateway, only()).deleteAllTicketsBySwimmingClassId(
          command.swimmingClassId());
      verify(cancelBoSwimmingClassDsGateway, times(1))
          .cancelAllReservationsBySwimmingClassIdAndReservationStatusIn(
              command.swimmingClassId(),
              List.of(RESERVATION_PENDING, PAYMENT_PENDING),
              CancellationReason.SWIMMING_CLASS_CANCELLED);
      verify(swimmingClass).cancel(command.cancelReason());
    }

    @Test
    @DisplayName("결제완료 또는 입금확인중인 예약이 있으면 예외를 발생시킨다")
    void shouldThrowExceptionWhenCompletedPaymentsExist() {
      // given
      val swimmingClass = mock(SwimmingClassEntity.class);
      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.of(swimmingClass));
      when(cancelBoSwimmingClassDsGateway.existsReservationBySwimmingClassIdReservationStatusIn(
          any(), any())).thenReturn(true);

      // when
      // then
      assertThatThrownBy(() -> interactor.cancelBoSwimmingClass(command))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("입금확인중, 결제완료 건이 있어 폐강이 불가합니다");

      verify(cancelBoSwimmingClassDsGateway, only())
          .existsReservationBySwimmingClassIdReservationStatusIn(
              command.swimmingClassId(),
              List.of(PAYMENT_VERIFICATION, PAYMENT_COMPLETED)
          );
      verify(updateBoSwimmingClassDsGateway, never()).deleteAllTicketsBySwimmingClassId(
          command.swimmingClassId());
      verify(cancelBoSwimmingClassDsGateway, never())
          .cancelAllReservationsBySwimmingClassIdAndReservationStatusIn(any(), any(), any());
      verify(swimmingClass, never()).cancel(any());
    }

    @Test
    @DisplayName("클래스가 존재하지 않으면 NoSuchElementException을 던진다")
    void shouldThrowExceptionWhenClassDoesNotExist() {
      // given
      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.empty());

      // when
      // then
      assertThatThrownBy(() -> interactor.cancelBoSwimmingClass(command))
          .isInstanceOf(NoSuchElementException.class);

      verifyNoInteractions(updateBoSwimmingClassDsGateway);
      verifyNoInteractions(cancelBoSwimmingClassDsGateway);
    }

    @Test
    @DisplayName("파라미터가 null이 전달되면 NullPointerException을 던진다")
    void shouldThrowExceptionWhenArgumentIsNull() {
      // when
      // then
      assertThatThrownBy(() -> interactor.cancelBoSwimmingClass(null))
          .isInstanceOf(NullPointerException.class);
    }

  }

}
