package com.project.swimcb.bo.reservation.adapter.in;

import static com.project.swimcb.bo.reservation.domain.BoReservation.Cancel;
import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.swimcb.bo.reservation.domain.BoReservation;
import com.project.swimcb.bo.reservation.domain.BoReservation.Member;
import com.project.swimcb.bo.reservation.domain.BoReservation.Payment;
import com.project.swimcb.bo.reservation.domain.BoReservation.Refund;
import com.project.swimcb.bo.reservation.domain.BoReservation.ReservationDetail;
import com.project.swimcb.bo.reservation.domain.BoReservation.SwimmingClass;
import com.project.swimcb.db.entity.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
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
      val reservation = TestBoReservationFactory.create(false, false);
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
    @DisplayName("취소 정보가 있는 경우 올바르게 매핑한다")
    void shouldMapReservationWithCancelCorrectly() {
      // given
      val reservation = TestBoReservationFactory.create(true, false);
      val page = new PageImpl<>(
          List.of(reservation),
          PageRequest.of(0, 10),
          1
      );

      // when
      val response = mapper.toResponse(page);

      // then
      assertThat(response).isNotNull();
      val mappedReservation = response.reservations().getContent().get(0);

      // 환불 정보 검증
      assertThat(mappedReservation.cancel()).isNotNull();
      assertThat(mappedReservation.cancel().canceledAt()).isEqualTo(
          reservation.cancel().canceledAt());
      assertThat(mappedReservation.cancel().reason()).isEqualTo(
          reservation.cancel().reason().getDescription());
    }

    @Test
    @DisplayName("환불 정보가 있는 경우 올바르게 매핑한다")
    void shouldMapReservationWithRefundCorrectly() {
      // given
      val reservation = TestBoReservationFactory.create(false, true);
      val page = new PageImpl<>(
          List.of(reservation),
          PageRequest.of(0, 10),
          1
      );

      // when
      val response = mapper.toResponse(page);

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

  private static class TestBoReservationFactory {

    private static BoReservation create(boolean includeCancel, boolean includeRefund) {
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
              .build())
          .payment(Payment.builder()
              .method(CASH_ON_SITE)
              .amount(50000)
              .pendingAt(LocalDateTime.now().minusDays(5))
              .verificationAt(LocalDateTime.now().minusDays(4))
              .completedAt(LocalDateTime.now().minusDays(3))
              .build());

      if (includeCancel) {
        builder.cancel(Cancel.builder()
            .reason(CancellationReason.USER_CANCELLED)
            .canceledAt(LocalDateTime.now().minusDays(2))
            .build()
        );
      }

      if (includeRefund) {
        builder.refund(Refund.builder()
            .amount(50000)
            .accountNo(new AccountNo("1234567890"))
            .bankName("국민은행")
            .accountHolder("홍길동")
            .refundedAt(LocalDateTime.now().minusDays(1))
            .build());
      }

      return builder.build();
    }
  }
}
