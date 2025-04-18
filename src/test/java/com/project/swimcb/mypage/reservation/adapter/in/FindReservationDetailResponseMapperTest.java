package com.project.swimcb.mypage.reservation.adapter.in;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
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
class FindReservationDetailResponseMapperTest {

  @InjectMocks
  private FindReservationDetailResponseMapper mapper;

  @Mock
  private ImageUrlPort imageUrlPort;

  @Test
  @DisplayName("ReservationDetail 객체를 FindReservationDetailResponse 객체로 변환한다")
  void shouldConvertToFindReservationDetailResponse() {
    // given
    val imageUrl = "DUMMY_IMAGE_URL";
    val detail = TestFindReservationDetailFactory.create();

    when(imageUrlPort.getImageUrl(anyString())).thenReturn(imageUrl);

    // when
    val response = mapper.toResponse(detail);

    // then
    assertThat(response).isNotNull();

    // SwimmingPool 검증
    assertThat(response.swimmingPool().id()).isEqualTo(detail.swimmingPool().id());
    assertThat(response.swimmingPool().imageUrl()).isEqualTo(imageUrl);
    assertThat(response.swimmingPool().accountNo()).isEqualTo(
        detail.swimmingPool().accountNo().value());

    // SwimmingClass 검증
    assertThat(response.swimmingClass().id()).isEqualTo(detail.swimmingClass().id());
    assertThat(response.swimmingClass().type()).isEqualTo(GROUP.getDescription());
    assertThat(response.swimmingClass().subType()).isEqualTo(detail.swimmingClass().subType());
    assertThat(response.swimmingClass().days()).isEqualTo(List.of("월", "수", "금"));

    // Ticket 검증
    assertThat(response.ticket().id()).isEqualTo(detail.ticket().id());
    assertThat(response.ticket().price()).isEqualTo(detail.ticket().price());

    // Reservation 검증
    assertThat(response.reservation().id()).isEqualTo(detail.reservation().id());
    assertThat(response.reservation().status()).isEqualTo(PAYMENT_PENDING.getDescription());
    assertThat(response.reservation().reservedAt()).isEqualTo(detail.reservation().reservedAt());
    assertThat(response.reservation().waitingNo()).isEqualTo(detail.reservation().waitingNo());

    // Cancel 검증
    assertThat(response.cancel().canceledAt()).isEqualTo(detail.cancel().canceledAt());

    // Refund 검증
    assertThat(response.refund().amount()).isEqualTo(detail.refund().amount());
    assertThat(response.refund().accountNo()).isNull();

    // Payment 검증
    assertThat(response.payment().method()).isEqualTo(CASH_ON_SITE.getDescription());
    assertThat(response.payment().pendingAt()).isEqualTo(detail.payment().pendingAt());

    // Review 검증
    assertThat(response.review().id()).isEqualTo(detail.review().id());
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
                  .daysOfWeek(new ClassDayOfWeek(List.of(MONDAY, WEDNESDAY, FRIDAY)))
                  .startTime(LocalTime.of(10, 0))
                  .endTime(LocalTime.of(11, 0))
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
          .cancel(
              ReservationDetail.Cancel.builder().build()
          )
          .refund(
              ReservationDetail.Refund.builder().build()
          )
          .review(
              ReservationDetail.Review.builder()
                  .id(5L)
                  .build()
          )
          .build();
    }
  }
}