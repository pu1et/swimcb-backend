package com.project.swimcb.bo.reservation.adapter.in;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.REFUND_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_CANCELLED;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.swimcb.bo.reservation.domain.BoReservation;
import com.project.swimcb.bo.reservation.domain.BoReservation.Member;
import com.project.swimcb.bo.reservation.domain.BoReservation.Payment;
import com.project.swimcb.bo.reservation.domain.BoReservation.Refund;
import com.project.swimcb.bo.reservation.domain.BoReservation.ReservationDetail;
import com.project.swimcb.bo.reservation.domain.BoReservation.SwimmingClass;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindBoReservationsResponseMapperTest {

  @InjectMocks
  private FindBoReservationsResponseMapper mapper;

  @Nested
  @DisplayName("toResponse 메서드는")
  class ToResponseMethod {

    @Test
    @DisplayName("기본 예약 정보를 응답 객체로 올바르게 매핑한다")
    void shouldMapReservationInfoToResponseCorrectly() {
      // given
      val reservation = TestBoReservationFactory.create(false);
      val page = new PageImpl<>(
          List.of(reservation),
          PageRequest.of(0, 10),
          1
      );

      // when
      val response = mapper.toResponse(page);

      // then
      assertThat(response).isNotNull();
      assertThat(response.reservations().getTotalElements()).isEqualTo(1);

      val mappedReservation = response.reservations().getContent().get(0);

      // 고객 정보 검증
      assertThat(mappedReservation.member().id()).isEqualTo(reservation.member().id());
      assertThat(mappedReservation.member().name()).isEqualTo(reservation.member().name());
      assertThat(mappedReservation.member().birthDate()).isEqualTo(
          reservation.member().birthDate());

      // 수영 클래스 정보 검증
      assertThat(mappedReservation.swimmingClass().id()).isEqualTo(
          reservation.swimmingClass().id());
      assertThat(mappedReservation.swimmingClass().type())
          .isEqualTo(reservation.swimmingClass().type().getDescription());
      assertThat(mappedReservation.swimmingClass().subType())
          .isEqualTo(reservation.swimmingClass().subType());

      // 예약 상세 정보 검증
      assertThat(mappedReservation.reservationDetail().id())
          .isEqualTo(reservation.reservationDetail().id());
      assertThat(mappedReservation.reservationDetail().ticketType())
          .isEqualTo(reservation.reservationDetail().ticketType());
      assertThat(mappedReservation.reservationDetail().status())
          .isEqualTo(reservation.reservationDetail().status().getDescription());

      // 결제 정보 검증
      assertThat(mappedReservation.payment().method())
          .isEqualTo(reservation.payment().method().getDescription());
      assertThat(mappedReservation.payment().amount())
          .isEqualTo(reservation.payment().amount());

      // 환불 정보는 null
      assertThat(mappedReservation.refund()).isNull();
    }

    @Test
    @DisplayName("환불 정보가 있는 경우 올바르게 매핑한다")
    void shouldMapReservationWithRefundCorrectly() {
      // given
      val reservation = TestBoReservationFactory.create(true);
      val page = new PageImpl<>(
          List.of(reservation),
          PageRequest.of(0, 10),
          1
      );

      // when
      FindBoReservationsResponse response = mapper.toResponse(page);

      // then
      assertThat(response).isNotNull();
      val mappedReservation = response.reservations().getContent().get(0);

      // 환불 정보 검증
      assertThat(mappedReservation.refund()).isNotNull();
      assertThat(mappedReservation.refund().amount()).isEqualTo(reservation.refund().amount());
      assertThat(mappedReservation.refund().accountNo()).isEqualTo(
          reservation.refund().accountNo().value());
      assertThat(mappedReservation.refund().bankName()).isEqualTo(reservation.refund().bankName());
      assertThat(mappedReservation.refund().accountHolder()).isEqualTo(
          reservation.refund().accountHolder());
    }
  }

  @Nested
  @DisplayName("lastStatusChangedAt 메서드는")
  class LastStatusChangedAtMethod {

    @Test
    @DisplayName("예약 상태에 따라 마지막 상태 변경 시간을 올바르게 결정한다")
    void shouldDetermineLastStatusChangedAtCorrectly() {
      // given
      val now = LocalDateTime.now();
      val pendingReservation = TestBoReservationFactory.createWithStatus(PAYMENT_PENDING, now);
      val completedReservation = TestBoReservationFactory.createWithStatus(PAYMENT_COMPLETED, now);
      val cancelledReservation = TestBoReservationFactory.createWithStatus(RESERVATION_CANCELLED,
          now);
      val refundedReservation = TestBoReservationFactory.createWithStatus(REFUND_COMPLETED, now);

      val page = new PageImpl<>(
          List.of(
              pendingReservation,
              completedReservation,
              cancelledReservation,
              refundedReservation
          ),
          PageRequest.of(0, 10),
          4
      );

      // when
      val response = mapper.toResponse(page);

      // then
      val reservations = response.reservations().getContent();

      assertThat(reservations.get(0).reservationDetail().lastStatusChangedAt())
          .isEqualTo(pendingReservation.reservationDetail().paymentPendingAt());

      assertThat(reservations.get(1).reservationDetail().lastStatusChangedAt())
          .isEqualTo(completedReservation.reservationDetail().paymentCompletedAt());

      assertThat(reservations.get(2).reservationDetail().lastStatusChangedAt())
          .isEqualTo(cancelledReservation.reservationDetail().canceledAt());

      assertThat(reservations.get(3).reservationDetail().lastStatusChangedAt())
          .isEqualTo(refundedReservation.reservationDetail().refundedAt());
    }
  }

  private static class TestBoReservationFactory {

    private static BoReservation create(boolean includeRefund) {
      var builder = BoReservation.builder()
          .member(Member.builder()
              .id(1L)
              .name("DUMMY_MEMBER_NAME")
              .birthDate(LocalDate.of(1990, 1, 1))
              .build())
          .swimmingClass(SwimmingClass.builder()
              .id(101L)
              .type(SwimmingClassTypeName.GROUP)
              .subType("DUMMY_CLASS_SUB_TYPE")
              .daysOfWeek(ClassDayOfWeek.of(3)) // 수요일
              .startTime(LocalTime.of(10, 0))
              .endTime(LocalTime.of(11, 0))
              .build())
          .reservationDetail(ReservationDetail.builder()
              .id(1001L)
              .ticketType(TicketType.SWIMMING_CLASS)
              .status(PAYMENT_COMPLETED)
              .reservedAt(LocalDateTime.now().minusDays(5))
              .paymentPendingAt(LocalDateTime.now().minusDays(5))
              .paymentCompletedAt(LocalDateTime.now().minusDays(4))
              .build())
          .payment(Payment.builder()
              .method(CASH_ON_SITE)
              .amount(50000)
              .build());

      if (includeRefund) {
        builder.refund(Refund.builder()
            .amount(50000)
            .accountNo(new AccountNo("1234567890"))
            .bankName("국민은행")
            .accountHolder("홍길동")
            .build());
      }

      return builder.build();
    }

    private static BoReservation createWithStatus(ReservationStatus status,
        LocalDateTime baseTime) {
      ReservationDetail.ReservationDetailBuilder detailBuilder = ReservationDetail.builder()
          .id(1001L)
          .ticketType(TicketType.SWIMMING_CLASS)
          .status(status)
          .reservedAt(baseTime.minusDays(5));

      // 상태에 따라 적절한 시간 설정
      switch (status) {
        case PAYMENT_PENDING -> detailBuilder.paymentPendingAt(baseTime.minusDays(4));
        case PAYMENT_COMPLETED -> {
          detailBuilder.paymentPendingAt(baseTime.minusDays(4));
          detailBuilder.paymentCompletedAt(baseTime.minusDays(3));
        }
        case RESERVATION_CANCELLED -> detailBuilder.canceledAt(baseTime.minusDays(2));
        case REFUND_COMPLETED -> {
          detailBuilder.paymentCompletedAt(baseTime.minusDays(3));
          detailBuilder.canceledAt(baseTime.minusDays(2));
          detailBuilder.refundedAt(baseTime.minusDays(1));
        }
      }

      return BoReservation.builder()
          .member(Member.builder()
              .id(1L)
              .name("DUMMY_MEMBER_NAME")
              .birthDate(LocalDate.of(1990, 1, 1))
              .build())
          .swimmingClass(SwimmingClass.builder()
              .id(101L)
              .type(SwimmingClassTypeName.GROUP)
              .subType("DUMMY_SUB_TYPE")
              .daysOfWeek(ClassDayOfWeek.of(3))
              .startTime(LocalTime.of(10, 0))
              .endTime(LocalTime.of(11, 0))
              .build())
          .reservationDetail(detailBuilder.build())
          .payment(Payment.builder()
              .method(CASH_ON_SITE)
              .amount(50000)
              .build())
          .build();
    }
  }
}