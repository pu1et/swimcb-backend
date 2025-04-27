package com.project.swimcb.bo.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.TicketType.SWIMMING_CLASS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.FindBoReservationsDsGateway;
import com.project.swimcb.bo.reservation.domain.BoReservation;
import com.project.swimcb.bo.reservation.domain.BoReservation.Member;
import com.project.swimcb.bo.reservation.domain.BoReservation.Payment;
import com.project.swimcb.bo.reservation.domain.BoReservation.ReservationDetail;
import com.project.swimcb.bo.reservation.domain.BoReservation.SwimmingClass;
import com.project.swimcb.bo.reservation.domain.FindBoReservationsCondition;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindBoReservationsInteractorTest {

  @InjectMocks
  private FindBoReservationsInteractor interactor;

  @Mock
  private FindBoReservationsDsGateway gateway;

  @Nested
  @DisplayName("findBoReservations 메서드는")
  class FindBoReservationsMethod {

    @Test
    @DisplayName("조건에 맞는 예약 목록을 성공적으로 조회한다")
    void returnReservationsSuccessfully() {
      // given
      val condition = TestFindBoReservationsConditionFactory.createDefault();
      val expectedResponse = TestBoReservationFactory.createPaged();

      when(gateway.findBoReservations(condition)).thenReturn(expectedResponse);

      // when
      val actualResponse = interactor.findBoReservations(condition);

      // then
      assertThat(actualResponse).isEqualTo(expectedResponse);
      verify(gateway, only()).findBoReservations(condition);
    }

    @Test
    @DisplayName("예약 상태 필터를 적용하여 조회한다")
    void returnReservationsWithStatusFilter() {
      // given
      val condition = TestFindBoReservationsConditionFactory.createWithStatus(PAYMENT_COMPLETED);
      val expectedResponse = TestBoReservationFactory.createPagedResponseWithStatus(
          PAYMENT_COMPLETED);

      when(gateway.findBoReservations(condition)).thenReturn(expectedResponse);

      // when
      val actualResponse = interactor.findBoReservations(condition);

      // then
      assertThat(actualResponse).isEqualTo(expectedResponse);
      verify(gateway, only()).findBoReservations(condition);
      assertThat(actualResponse.getContent().get(0).reservationDetail().status())
          .isEqualTo(PAYMENT_COMPLETED);
    }

    @Test
    @DisplayName("결제 방법 필터를 적용하여 조회한다")
    void returnReservationsWithPaymentMethodFilter() {
      // given
      val condition = TestFindBoReservationsConditionFactory.createWithPaymentMethod(CASH_ON_SITE);
      val expectedResponse = TestBoReservationFactory.createPagedWithPaymentMethod(CASH_ON_SITE);

      when(gateway.findBoReservations(condition)).thenReturn(expectedResponse);

      // when
      val actualResponse = interactor.findBoReservations(condition);

      // then
      assertThat(actualResponse).isEqualTo(expectedResponse);
      verify(gateway, only()).findBoReservations(condition);
      assertThat(actualResponse.getContent().get(0).payment().method())
          .isEqualTo(CASH_ON_SITE);
    }

    @Test
    @DisplayName("프로그램 타입 필터를 적용하여 조회한다")
    void returnReservationsWithProgramTypeFilter() {
      // given
      val condition = TestFindBoReservationsConditionFactory.createWithProgramType(SWIMMING_CLASS);
      val expectedResponse = TestBoReservationFactory.createPagedWithProgramType(SWIMMING_CLASS);

      when(gateway.findBoReservations(condition)).thenReturn(expectedResponse);

      // when
      val actualResponse = interactor.findBoReservations(condition);

      // then
      assertThat(actualResponse).isEqualTo(expectedResponse);
      verify(gateway, only()).findBoReservations(condition);
      assertThat(actualResponse.getContent().get(0).reservationDetail().ticketType())
          .isEqualTo(SWIMMING_CLASS);
    }
  }

  private static class TestFindBoReservationsConditionFactory {

    private static FindBoReservationsCondition createDefault() {
      return FindBoReservationsCondition.builder()
          .swimmingPoolId(1L)
          .startDate(LocalDate.of(2025, 1, 1))
          .endDate(LocalDate.of(2025, 12, 31))
          .pageable(PageRequest.of(0, 10))
          .build();
    }

    private static FindBoReservationsCondition createWithStatus(ReservationStatus status) {
      return FindBoReservationsCondition.builder()
          .swimmingPoolId(1L)
          .startDate(LocalDate.of(2025, 1, 1))
          .endDate(LocalDate.of(2025, 12, 31))
          .reservationStatus(status)
          .pageable(PageRequest.of(0, 10))
          .build();
    }

    private static FindBoReservationsCondition createWithPaymentMethod(PaymentMethod method) {
      return FindBoReservationsCondition.builder()
          .swimmingPoolId(1L)
          .startDate(LocalDate.of(2025, 1, 1))
          .endDate(LocalDate.of(2025, 12, 31))
          .paymentMethod(method)
          .pageable(PageRequest.of(0, 10))
          .build();
    }

    private static FindBoReservationsCondition createWithProgramType(TicketType type) {
      return FindBoReservationsCondition.builder()
          .swimmingPoolId(1L)
          .startDate(LocalDate.of(2025, 1, 1))
          .endDate(LocalDate.of(2025, 12, 31))
          .programType(type)
          .pageable(PageRequest.of(0, 10))
          .build();
    }
  }

  private static class TestBoReservationFactory {

    private static Page<BoReservation> createPaged() {
      val reservation = createDefaultReservation();
      return new PageImpl<>(List.of(reservation), PageRequest.of(0, 10), 1);
    }

    private static PageImpl<BoReservation> createPagedResponseWithStatus(ReservationStatus status) {
      val reservation = createWithStatus(status);
      return new PageImpl<>(List.of(reservation), PageRequest.of(0, 10), 1);
    }

    private static PageImpl<BoReservation> createPagedWithPaymentMethod(
        PaymentMethod method) {
      val reservation = createWithPaymentMethod(method);
      return new PageImpl<>(List.of(reservation), PageRequest.of(0, 10), 1);
    }

    private static PageImpl<BoReservation> createPagedWithProgramType(TicketType type) {
      val reservation = createWithProgramType(type);
      return new PageImpl<>(List.of(reservation), PageRequest.of(0, 10), 1);
    }

    private static BoReservation createDefaultReservation() {
      return BoReservation.builder()
          .member(Member.builder()
              .id(1L)
              .name("DUMMY_MEMBER_NAME")
              .birthDate(LocalDate.of(1990, 1, 1))
              .build())
          .swimmingClass(SwimmingClass.builder()
              .id(101L)
              .type(SwimmingClassTypeName.GROUP)
              .subType("DUMMY_CLASS_SUB_TYPE")
              .daysOfWeek(ClassDayOfWeek.of(0b1010100)) // 월, 수, 금
              .startTime(LocalTime.of(10, 0))
              .endTime(LocalTime.of(11, 0))
              .build())
          .reservationDetail(ReservationDetail.builder()
              .id(1001L)
              .ticketType(SWIMMING_CLASS)
              .status(PAYMENT_COMPLETED)
              .reservedAt(LocalDateTime.now().minusDays(5))
              .paymentCompletedAt(LocalDateTime.now().minusDays(4))
              .build())
          .payment(Payment.builder()
              .method(CASH_ON_SITE)
              .amount(50000)
              .build())
          .build();
    }

    private static BoReservation createWithStatus(ReservationStatus status) {
      val defaultRes = createDefaultReservation();

      val newDetail = ReservationDetail.builder()
          .id(defaultRes.reservationDetail().id())
          .ticketType(defaultRes.reservationDetail().ticketType())
          .status(status)
          .reservedAt(defaultRes.reservationDetail().reservedAt())
          .paymentCompletedAt(defaultRes.reservationDetail().paymentCompletedAt())
          .build();

      return BoReservation.builder()
          .member(defaultRes.member())
          .swimmingClass(defaultRes.swimmingClass())
          .reservationDetail(newDetail)
          .payment(defaultRes.payment())
          .build();
    }

    private static BoReservation createWithPaymentMethod(PaymentMethod method) {
      val defaultRes = createDefaultReservation();

      val newPayment = Payment.builder()
          .method(method) // Use the provided payment method
          .amount(defaultRes.payment().amount())
          .build();

      return BoReservation.builder()
          .member(defaultRes.member())
          .swimmingClass(defaultRes.swimmingClass())
          .reservationDetail(defaultRes.reservationDetail())
          .payment(newPayment)
          .build();
    }

    private static BoReservation createWithProgramType(TicketType type) {
      val defaultRes = createDefaultReservation();

      val newDetail = ReservationDetail.builder()
          .id(defaultRes.reservationDetail().id())
          .ticketType(type) // Use the provided ticket type
          .status(defaultRes.reservationDetail().status())
          .reservedAt(defaultRes.reservationDetail().reservedAt())
          .paymentCompletedAt(defaultRes.reservationDetail().paymentCompletedAt())
          .build();

      return BoReservation.builder()
          .member(defaultRes.member())
          .swimmingClass(defaultRes.swimmingClass())
          .reservationDetail(newDetail)
          .payment(defaultRes.payment())
          .build();
    }
  }
}