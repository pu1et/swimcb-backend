package com.project.swimcb.swimmingpool.domain;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.BANK_TRANSFER;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.TicketType.SWIMMING_CLASS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.project.swimcb.db.entity.ReservationEntity;
import com.project.swimcb.db.entity.SwimmingClassEntity;
import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import java.time.LocalDateTime;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReservationTest {

  @Nested
  @DisplayName("수영 클래스 일반예약 생성")
  class CreateSwimmingClassReservation {

    private final long TICKET_ID = 1L;
    private final PaymentMethod PAYMENT_METHOD = BANK_TRANSFER;
    private MemberEntity member = mock(MemberEntity.class);
    private final SwimmingClassEntity swimmingClass = mock(SwimmingClassEntity.class);

    @Test
    @DisplayName("수영 클래스 일반 예약을 성공적으로 생성한다.")
    void createSwimmingClassReservation() {
      // Given
      val beforeCreation = LocalDateTime.now();
      // When
      val reservation = ReservationEntity.createClassNormalReservation()
          .member(member)
          .swimmingClass(swimmingClass)
          .ticketId(TICKET_ID)
          .paymentMethod(PAYMENT_METHOD)
          .build();
      // Then
      assertThat(reservation.getMember()).isEqualTo(member);
      assertThat(reservation.getSwimmingClass()).isEqualTo(swimmingClass);
      assertThat(reservation.getTicketId()).isEqualTo(TICKET_ID);
      assertThat(reservation.getTicketType()).isEqualTo(SWIMMING_CLASS);
      assertThat(reservation.getPaymentMethod()).isEqualTo(PAYMENT_METHOD);
      assertThat(reservation.getReservationStatus()).isEqualTo(PAYMENT_PENDING);
      assertThat(reservation.getReservedAt()).isAfterOrEqualTo(beforeCreation);
    }

    @Test
    @DisplayName("회원 정보가 null이면 NPE가 발생한다.")
    void createSwimmingClassReservationWithNullMember() {
      // given
      // when
      // then
      assertThatThrownBy(() -> ReservationEntity.createClassNormalReservation()
          .member(null)
          .swimmingClass(swimmingClass)
          .ticketId(TICKET_ID)
          .paymentMethod(PAYMENT_METHOD)
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("결제 수단이 null이면 NPE가 발생한다.")
    void createSwimmingClassReservationWithNullPaymentMethod() {
      // given
      // when
      // then
      assertThatThrownBy(() -> ReservationEntity.createClassNormalReservation()
          .member(member)
          .swimmingClass(swimmingClass)
          .ticketId(TICKET_ID)
          .paymentMethod(null)
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("수영장 ID가 null이면 NPE가 발생한다.")
    void createSwimmingClassReservationWithNullSwimmingClassId() {
      // given
      // when
      // then
      assertThatThrownBy(() -> ReservationEntity.createClassNormalReservation()
          .member(member)
          .swimmingClass(null)
          .ticketId(TICKET_ID)
          .paymentMethod(PAYMENT_METHOD)
          .build())
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("수영 클래스 대기예약 생성")
  class CreateSwimmingClassWaitingReservation {

    private final long TICKET_ID = 1L;
    private final PaymentMethod PAYMENT_METHOD = BANK_TRANSFER;
    private final MemberEntity member = mock(MemberEntity.class);
    private final SwimmingClassEntity swimmingClass = mock(SwimmingClassEntity.class);

    @Test
    @DisplayName("수영 클래스 대기 예약을 성공적으로 생성한다.")
    void createSwimmingClassReservation() {
      // Given
      val beforeCreation = LocalDateTime.now();
      // When
      val reservation = ReservationEntity.createClassWaitingReservation()
          .member(member)
          .swimmingClass(swimmingClass)
          .ticketId(TICKET_ID)
          .paymentMethod(PAYMENT_METHOD)
          .build();
      // Then
      assertThat(reservation.getMember()).isEqualTo(member);
      assertThat(reservation.getSwimmingClass()).isEqualTo(swimmingClass);
      assertThat(reservation.getTicketId()).isEqualTo(TICKET_ID);
      assertThat(reservation.getTicketType()).isEqualTo(SWIMMING_CLASS);
      assertThat(reservation.getPaymentMethod()).isEqualTo(PAYMENT_METHOD);
      assertThat(reservation.getReservationStatus()).isEqualTo(RESERVATION_PENDING);
      assertThat(reservation.getReservedAt()).isAfterOrEqualTo(beforeCreation);
    }

    @Test
    @DisplayName("회원 정보가 null이면 NPE가 발생한다.")
    void createSwimmingClassReservationWithNullMember() {
      // given
      // when
      // then
      assertThatThrownBy(() -> ReservationEntity.createClassWaitingReservation()
          .member(null)
          .swimmingClass(swimmingClass)
          .ticketId(TICKET_ID)
          .paymentMethod(PAYMENT_METHOD)
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("결제 수단이 null이면 NPE가 발생한다.")
    void createSwimmingClassReservationWithNullPaymentMethod() {
      // given
      // when
      // then
      assertThatThrownBy(() -> ReservationEntity.createClassWaitingReservation()
          .member(member)
          .swimmingClass(swimmingClass)
          .ticketId(TICKET_ID)
          .paymentMethod(null)
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("수영장 ID가 null이면 NPE가 발생한다.")
    void createSwimmingClassReservationWithNullSwimmingClassId() {
      // given
      // when
      // then
      assertThatThrownBy(() -> ReservationEntity.createClassWaitingReservation()
          .member(member)
          .swimmingClass(null)
          .ticketId(TICKET_ID)
          .paymentMethod(PAYMENT_METHOD)
          .build())
          .isInstanceOf(NullPointerException.class);
    }
  }
}
