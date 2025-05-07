package com.project.swimcb.mypage.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.BANK_TRANSFER;
import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimmingpool.domain.Reservation;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
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
class ChangeReservationPaymentMethodInteractorTest {

  @InjectMocks
  private ChangeReservationPaymentMethodInteractor interactor;

  @Mock
  private ReservationRepository reservationRepository;

  private static Long MEMBER_ID;
  private static Long RESERVATION_ID;
  private static PaymentMethod NEW_PAYMENT_METHOD;
  private static Reservation RESERVATION;

  @BeforeEach
  void setUp() {
    MEMBER_ID = 1L;
    RESERVATION_ID = 2L;
    NEW_PAYMENT_METHOD = CASH_ON_SITE;
    RESERVATION = mock(Reservation.class);
  }

  @Nested
  @DisplayName("결제수단 변경 시")
  class ChangePaymentMethodTest {

    @Test
    @DisplayName("예약이 존재하고 결제수단을 변경할 수 있는 상태면 성공적으로 변경된다")
    void shouldChangePaymentMethodSuccessfully() {
      // given
      when(reservationRepository.findByIdAndMemberId(anyLong(), anyLong()))
          .thenReturn(Optional.of(RESERVATION));
      when(RESERVATION.canChangePaymentMethod()).thenReturn(true);

      // when
      interactor.changePaymentMethod(MEMBER_ID, RESERVATION_ID, NEW_PAYMENT_METHOD);

      // then
      verify(reservationRepository, only()).findByIdAndMemberId(RESERVATION_ID, MEMBER_ID);
      verify(RESERVATION, times(1)).canChangePaymentMethod();
      verify(RESERVATION, times(1)).changePaymentMethod(NEW_PAYMENT_METHOD);
    }

    @Test
    @DisplayName("예약이 존재하지 않으면 NoSuchElementException을 던진다")
    void shouldThrowExceptionWhenReservationDoesNotExist() {
      // given
      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.empty());

      // when
      // then
      assertThatThrownBy(() ->
          interactor.changePaymentMethod(MEMBER_ID, RESERVATION_ID, NEW_PAYMENT_METHOD))
          .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("결제수단이 변경 불가능한 상태이면 IllegalStateException을 던진다")
    void shouldThrowExceptionWhenReservationCannotBeChanged() {
      // given
      when(reservationRepository.findByIdAndMemberId(anyLong(), anyLong()))
          .thenReturn(Optional.of(RESERVATION));
      when(RESERVATION.canChangePaymentMethod()).thenReturn(false);

      // when
      // then
      assertThatThrownBy(() ->
          interactor.changePaymentMethod(MEMBER_ID, RESERVATION_ID, NEW_PAYMENT_METHOD))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("인자로 null이 전달되면 NullPointerException을 던진다")
    void shouldThrowExceptionWhenArgumentIsNull() {
      // when, then
      assertThatThrownBy(() ->
          interactor.changePaymentMethod(null, RESERVATION_ID, BANK_TRANSFER))
          .isInstanceOf(NullPointerException.class);

      assertThatThrownBy(() ->
          interactor.changePaymentMethod(MEMBER_ID, null, BANK_TRANSFER))
          .isInstanceOf(NullPointerException.class);

      assertThatThrownBy(() ->
          interactor.changePaymentMethod(MEMBER_ID, RESERVATION_ID, null))
          .isInstanceOf(NullPointerException.class);
    }
  }
}