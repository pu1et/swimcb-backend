package com.project.swimcb.mypage.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.mypage.reservation.application.port.out.FindReservationDetailGateway;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import java.time.LocalDateTime;
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
class FindReservationDetailInteractorTest {

  @InjectMocks
  private FindReservationDetailInteractor interactor;

  @Mock
  private FindReservationDetailGateway gateway;

  @Test
  @DisplayName("예약 ID로 예약 상세 정보를 조회한다")
  void findReservationDetail() {
    // given
    val reservationId = 1L;
    val detail = TestFindReservationDetailFactory.create();
    when(gateway.findReservationDetail(reservationId)).thenReturn(detail);

    // when
    val result = interactor.findReservationDetail(reservationId);

    // then
    verify(gateway).findReservationDetail(reservationId);

    assertThat(result).isEqualTo(detail);
  }

  private static class TestFindReservationDetailFactory {

    private static ReservationDetail create() {
      return ReservationDetail.builder()
          .swimmingPool(
              ReservationDetail.SwimmingPool.builder()
                  .id(1L)
                  .name("DUMMY_POOL_NAME")
                  .phone("DUMMY_POOL_PHONE")
                  .imagePath("DUMMY_POOL_IMAGE_PATH")
                  .accountNo(AccountNo.of("DUMMY_ACCOUNT_NO"))
                  .build()
          )
          .swimmingClass(
              ReservationDetail.SwimmingClass.builder()
                  .id(2L)
                  .month(3)
                  .type(SwimmingClassTypeName.GROUP)
                  .subType("DUMMY_CLASS_SUB_TYPE")
                  .daysOfWeek(new ClassDayOfWeek(List.of()))
                  .startTime(LocalTime.of(10, 0))
                  .endTime(LocalTime.of(11, 0))
                  .isCanceled(false)
                  .build()
          )
          .ticket(
              ReservationDetail.Ticket.builder()
                  .id(3L)
                  .name("DUMMY_TICKET_NAME")
                  .price(50000)
                  .build()
          )
          .reservation(
              ReservationDetail.Reservation.builder()
                  .id(4L)
                  .status(PAYMENT_PENDING)
                  .reservedAt(LocalDateTime.MIN)
                  .build()
          )
          .payment(
              ReservationDetail.Payment.builder()
                  .method(CASH_ON_SITE)
                  .amount(50000)
                  .build()
          )
          .build();
    }
  }
}
