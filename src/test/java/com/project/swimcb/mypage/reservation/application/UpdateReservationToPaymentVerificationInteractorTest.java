package com.project.swimcb.mypage.reservation.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.db.entity.ReservationEntity;
import com.project.swimcb.db.repository.ReservationRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateReservationToPaymentVerificationInteractorTest {

  @InjectMocks
  private UpdateReservationToPaymentVerificationInteractor interactor;

  @Mock
  private ReservationRepository reservationRepository;

  private Long memberId;
  private Long reservationId;
  private ReservationEntity reservation;

  @BeforeEach
  void setUp() {
    memberId = 1L;
    reservationId = 2L;

    reservation = mock(ReservationEntity.class);
  }

  @Nested
  @DisplayName("예약을 입금확인중으로 변경할 때")
  class UpdateReservationToPaymentVerification {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {

      @Test
      @DisplayName("입금확인중 상태로 변경할 수 있으면 상태를 변경한다")
      void shouldUpdateReservationToPaymentVerification() {
        // given
        when(reservationRepository.findByIdAndMemberId(reservationId, memberId))
            .thenReturn(Optional.of(reservation));
        when(reservation.canTransitionToPaymentVerificationByUser()).thenReturn(true);

        // when
        interactor.updateReservationToPaymentVerification(memberId, reservationId);

        // then
        verify(reservationRepository, only()).findByIdAndMemberId(reservationId, memberId);
        verify(reservation, times(1)).canTransitionToPaymentVerificationByUser();
        verify(reservation, times(1)).updateStatusToPaymentVerification();
      }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCase {

      @Test
      @DisplayName("예약이 존재하지 않으면 예외가 발생한다")
      void shouldThrowExceptionWhenReservationNotFound() {
        // given
        when(reservationRepository.findByIdAndMemberId(reservationId, memberId))
            .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(
            () -> interactor.updateReservationToPaymentVerification(memberId, reservationId))
            .isInstanceOf(NoSuchElementException.class);
      }

      @Test
      @DisplayName("입금확인중 상태로 변경할 수 없으면 예외가 발생한다")
      void shouldThrowExceptionWhenReservationStatusIsNotPaymentPending() {
        // given
        when(reservationRepository.findByIdAndMemberId(reservationId, memberId))
            .thenReturn(Optional.of(reservation));

        // when
        // then
        assertThatThrownBy(
            () -> interactor.updateReservationToPaymentVerification(memberId, reservationId))
            .isInstanceOf(IllegalStateException.class);
      }
    }
  }
}
