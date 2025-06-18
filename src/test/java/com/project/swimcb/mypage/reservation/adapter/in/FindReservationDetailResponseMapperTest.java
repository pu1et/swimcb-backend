package com.project.swimcb.mypage.reservation.adapter.in;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.db.entity.AccountNo;
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
  @DisplayName("기본 예약 정보가 올바르게 응답으로 변환된다")
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

    // Payment 검증
    assertThat(response.payment().method()).isEqualTo(CASH_ON_SITE.getDescription());
    assertThat(response.payment().pendingAt()).isEqualTo(detail.payment().pendingAt());

    // Cancel 검증
    assertThat(response.cancel().canceledAt()).isEqualTo(detail.cancel().canceledAt());

    // Refund 검증
    assertThat(response.refund().amount()).isEqualTo(detail.refund().amount());
    assertThat(response.refund().accountNo()).isNotNull();

    // Review 검증
    assertThat(response.review().id()).isEqualTo(detail.review().id());
  }

  @Test
  @DisplayName("취소 정보가 있는 경우 올바르게 매핑된다")
  void shouldMapCancelInformation() {
    // given
    val imageUrl = "https://example.com/image.jpg";
    val detail = TestFindReservationDetailFactory.createCancel();

    when(imageUrlPort.getImageUrl(anyString())).thenReturn(imageUrl);

    // when
    val response = mapper.toResponse(detail);

    // then
    assertThat(response.cancel()).isNotNull();
    assertThat(response.cancel().canceledAt()).isEqualTo(detail.cancel().canceledAt());

    // 취소된 경우 환불 정보는 없어야 함
    assertThat(response.refund()).isNull();
  }

  @Test
  @DisplayName("환불 정보가 있는 경우 올바르게 매핑된다")
  void shouldMapRefundInformation() {
    // given
    val imageUrl = "https://example.com/image.jpg";
    val detail = TestFindReservationDetailFactory.createRefund();

    when(imageUrlPort.getImageUrl(anyString())).thenReturn(imageUrl);

    // when
    val response = mapper.toResponse(detail);

    // then
    assertThat(response.refund()).isNotNull();
    assertThat(response.refund().amount()).isEqualTo(detail.refund().amount());
    assertThat(response.refund().bankName()).isEqualTo(detail.refund().bankName());
    assertThat(response.refund().accountNo()).isEqualTo(detail.refund().accountNo().value());
    assertThat(response.refund().refundedAt()).isEqualTo(detail.refund().refundedAt());

    // 환불된 경우 취소 정보는 없어야 함
    assertThat(response.cancel()).isNull();
  }

  @Test
  @DisplayName("리뷰 정보가 있는 경우 올바르게 매핑된다")
  void shouldMapReviewInformation() {
    // given
    val imageUrl = "https://example.com/image.jpg";
    val detail = TestFindReservationDetailFactory.createWithReview();

    when(imageUrlPort.getImageUrl(anyString())).thenReturn(imageUrl);

    // when
    val response = mapper.toResponse(detail);

    // then
    assertThat(response.review()).isNotNull();
    assertThat(response.review().id()).isEqualTo(detail.review().id());
  }

  @Test
  @DisplayName("예약 정보가 null이면 NullPointerException이 발생한다")
  void shouldThrowNullPointerExceptionForNullInput() {
    // when
    // then
    assertThatThrownBy(() -> mapper.toResponse(null))
        .isInstanceOf(NullPointerException.class);
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
                  .reservedAt(LocalDateTime.of(2025, 5, 1, 10, 0))
                  .waitingNo(null)
                  .build()
          )
          .payment(
              ReservationDetail.Payment.builder()
                  .method(CASH_ON_SITE)
                  .amount(50000)
                  .pendingAt(LocalDateTime.of(2025, 5, 2, 10, 0))
                  .approvedAt(null)
                  .build()
          )
          .cancel(
              ReservationDetail.Cancel.builder()
                  .canceledAt(LocalDateTime.of(2025, 5, 3, 10, 0))
                  .build()
          )
          .refund(
              ReservationDetail.Refund.builder()
                  .amount(45000)
                  .accountNo(new AccountNo("DUMMY_ACCOUNT_NO"))
                  .bankName("DUMMY_BANK")
                  .refundedAt(LocalDateTime.of(2025, 5, 4, 10, 0))
                  .build()
          )
          .review(
              ReservationDetail.Review.builder()
                  .id(5L)
                  .build()
          )
          .build();
    }

    private static ReservationDetail createCancel() {
      val detail = create();
      return ReservationDetail.builder()
          .swimmingPool(detail.swimmingPool())
          .swimmingClass(detail.swimmingClass())
          .ticket(detail.ticket())
          .reservation(detail.reservation())
          .payment(detail.payment())
          .cancel(detail.cancel())
          .build();
    }

    private static ReservationDetail createRefund() {
      val detail = create();
      return ReservationDetail.builder()
          .swimmingPool(detail.swimmingPool())
          .swimmingClass(detail.swimmingClass())
          .ticket(detail.ticket())
          .reservation(detail.reservation())
          .payment(detail.payment())
          .refund(detail.refund())
          .build();
    }

    private static ReservationDetail createWithoutReview() {
      val detail = create();
      return ReservationDetail.builder()
          .swimmingPool(detail.swimmingPool())
          .swimmingClass(detail.swimmingClass())
          .ticket(detail.ticket())
          .reservation(detail.reservation())
          .payment(detail.payment())
          .review(null)
          .build();
    }

    private static ReservationDetail createWithReview() {
      val detail = create();
      return ReservationDetail.builder()
          .swimmingPool(detail.swimmingPool())
          .swimmingClass(detail.swimmingClass())
          .ticket(detail.ticket())
          .reservation(detail.reservation())
          .payment(detail.payment())
          .review(detail.review())
          .build();
    }
  }
}
