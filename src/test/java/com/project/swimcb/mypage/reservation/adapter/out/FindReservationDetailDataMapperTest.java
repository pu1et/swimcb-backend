package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.REFUND_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_CANCELLED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.FindReservationDetailDataMapper.QueryReservationDetail;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindReservationDetailDataMapperTest {

  @InjectMocks
  private FindReservationDetailDataMapper mapper;

  @Mock
  private JPAQueryFactory queryFactory;

  @Mock
  private JPAQuery<QueryReservationDetail> query;

  @Nested
  @DisplayName("예약 상세 정보 조회 시")
  class FindReservationDetailTest {

    @Test
    @DisplayName("유효한 예약 ID로 조회하면 예약 상세 정보를 반환한다")
    void returnReservationDetailWithValidId() {
      // given
      val reservationId = 1L;
      val queryResult = TestQueryReservationDetailFactory.create();

      // QueryDSL 체인 설정
      setupQueryChain(queryResult);

      // when
      val result = mapper.findReservationDetail(reservationId);

      // then
      assertThat(result).isNotNull();

      // SwimmingPool 검증
      assertThat(result.swimmingPool().id()).isEqualTo(queryResult.swimmingPoolId());
      assertThat(result.swimmingPool().name()).isEqualTo(queryResult.swimmingPoolName());
      assertThat(result.swimmingPool().imagePath()).isEqualTo(queryResult.swimmingPoolImagePath());
      assertThat(result.swimmingPool().accountNo()).isEqualTo(queryResult.accountNo());

      // SwimmingClass 검증
      assertThat(result.swimmingClass().id()).isEqualTo(queryResult.swimmingClassId());
      assertThat(result.swimmingClass().month()).isEqualTo(queryResult.month());
      assertThat(result.swimmingClass().type()).isEqualTo(queryResult.swimmingClassType());
      assertThat(result.swimmingClass().subType()).isEqualTo(queryResult.swimmingClassSubType());
      assertThat(result.swimmingClass().daysOfWeek().value())
          .containsExactly(MONDAY, WEDNESDAY, FRIDAY);
      assertThat(result.swimmingClass().startTime()).isEqualTo(queryResult.startTime());
      assertThat(result.swimmingClass().endTime()).isEqualTo(queryResult.endTime());

      // Ticket 검증
      assertThat(result.ticket().id()).isEqualTo(queryResult.ticketId());
      assertThat(result.ticket().name()).isEqualTo(queryResult.ticketName());
      assertThat(result.ticket().price()).isEqualTo(queryResult.ticketPrice());

      // Reservation 검증
      assertThat(result.reservation().id()).isEqualTo(reservationId);
      assertThat(result.reservation().status()).isEqualTo(queryResult.reservationStatus());
      assertThat(result.reservation().reservedAt()).isEqualTo(queryResult.reservedAt());
      assertThat(result.reservation().waitingNo()).isEqualTo(queryResult.waitingNo());

      // Payment 검증
      assertThat(result.payment().method()).isEqualTo(queryResult.paymentMethod());
      assertThat(result.payment().amount()).isEqualTo(queryResult.paymentAmount());

      // Review 검증
      assertThat(result.review().id()).isEqualTo(queryResult.reviewId());
    }

    @Test
    @DisplayName("예약 상세 정보가 조회되지 않을 때 예외가 발생한다")
    void throwExceptionWhenReservationDetailNotFound() {
      // given
      val reservationId = 999L;

      // QueryDSL 체인 설정 - 결과 없음
      setupQueryChain(null);

      // when
      // then
      assertThatThrownBy(() -> mapper.findReservationDetail(reservationId))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessageContaining("예약 상세 정보가 존재하지 않습니다");
    }

    @Test
    @DisplayName("예약 취소된 경우 취소 정보만 포함되고 환불 정보는 포함되지 않아야 한다")
    void shouldIncludeCancelInfoWhenReservationCancelled() {
      // given
      val reservationId = 1L;
      val result = TestQueryReservationDetailFactory.create();

      setupQueryChain(result);

      when(result.reservationStatus()).thenReturn(RESERVATION_CANCELLED);
      when(result.canceledAt()).thenReturn(LocalDateTime.of(2025, 4, 1, 10, 0, 0));

      // when
      val detail = mapper.findReservationDetail(reservationId);

      // then
      assertThat(detail.cancel()).isNotNull();
      assertThat(detail.cancel().canceledAt()).isEqualTo(result.canceledAt());

      assertThat(detail.refund()).isNull();
    }

    @Test
    @DisplayName("환불 완료된 경우 환불 정보만 포함되고 취소 정보는 포함되지 않아야 한다")
    void shouldIncludeRefundInfoWhenRefundCompleted() {
      // given
      val reservationId = 1L;
      val result = TestQueryReservationDetailFactory.create();

      setupQueryChain(result);

      when(result.reservationStatus()).thenReturn(REFUND_COMPLETED);
      when(result.refundAmount()).thenReturn(10000);
      when(result.refundAccountNo()).thenReturn(new AccountNo("DUMMY_ACCOUNT_NO"));
      when(result.refundBankName()).thenReturn("DUMMY_BANK_NAME");
      when(result.refundedAt()).thenReturn(LocalDateTime.of(2025, 4, 1, 10, 0, 0));

      // when
      val detail = mapper.findReservationDetail(reservationId);

      // then
      assertThat(detail.refund()).isNotNull();
      assertThat(detail.refund().amount()).isEqualTo(result.refundAmount());
      assertThat(detail.refund().accountNo()).isEqualTo(result.refundAccountNo());
      assertThat(detail.refund().bankName()).isEqualTo(result.refundBankName());
      assertThat(detail.refund().refundedAt()).isEqualTo(result.refundedAt());

      assertThat(detail.cancel()).isNull();
    }

    @Test
    @DisplayName("리뷰 정보가 없는 경우 리뷰 정보는 포함되지 않아야 한다.")
    void shouldNotIncludeReviewInfoWhenReviewNotExists() {
      // given
      val reservationId = 1L;
      val result = TestQueryReservationDetailFactory.create();

      setupQueryChain(result);

      when(result.reviewId()).thenReturn(null);

      // when
      val detail = mapper.findReservationDetail(reservationId);

      // then
      assertThat(detail.review()).isNull();
    }

    @Test
    @DisplayName("리뷰 정보가 있는 경우 리뷰 정보가 포함되어야 한다")
    void shouldIncludeReviewInfoWhenReviewExists() {
      // given
      val reservationId = 1L;
      val result = TestQueryReservationDetailFactory.create();

      setupQueryChain(result);

      when(result.reviewId()).thenReturn(2L);

      // when
      val detail = mapper.findReservationDetail(reservationId);

      // then
      assertThat(detail.review()).isNotNull();
      assertThat(detail.review().id()).isEqualTo(result.reviewId());
    }

    /**
     * QueryDSL 체인을 설정하는 메서드
     */
    private void setupQueryChain(QueryReservationDetail result) {
      when(queryFactory.select(any(Expression.class))).thenReturn(query);
      when(query.from(any(EntityPath.class))).thenReturn(query);
      when(query.join(any(EntityPath.class))).thenReturn(query);
      when(query.leftJoin(any(EntityPath.class))).thenReturn(query);
      when(query.on(any(Predicate.class))).thenReturn(query);
      when(query.where(any(Predicate.class))).thenReturn(query);
      when(query.fetchFirst()).thenReturn(result);
    }
  }

  private static class TestQueryReservationDetailFactory {

    private static QueryReservationDetail create() {
      val detail = mock(QueryReservationDetail.class);
      when(detail.swimmingPoolId()).thenReturn(1L);
      when(detail.swimmingPoolName()).thenReturn("DUMMY_POOL_NAME");
      when(detail.swimmingPoolPhone()).thenReturn("DUMMY_POOL_PHONE");
      when(detail.swimmingPoolImagePath()).thenReturn("DUMMY_IMAGE_PATH");
      when(detail.accountNo()).thenReturn(new AccountNo("DUMMY_ACCOUNT_NO"));
      when(detail.swimmingClassId()).thenReturn(2L);
      when(detail.month()).thenReturn(3);
      when(detail.swimmingClassType()).thenReturn(GROUP);
      when(detail.swimmingClassSubType()).thenReturn("DUMMY_CLASS_SUB_TYPE");
      when(detail.daysOfWeek()).thenReturn(0b1010100);
      when(detail.startTime()).thenReturn(LocalTime.of(10, 0));
      when(detail.endTime()).thenReturn(LocalTime.of(11, 0));
      when(detail.ticketId()).thenReturn(3L);
      when(detail.ticketName()).thenReturn("DUMMY_TICKET_NAME");
      when(detail.ticketPrice()).thenReturn(50000);
      when(detail.reservationStatus()).thenReturn(RESERVATION_PENDING);
      when(detail.reservedAt()).thenReturn(LocalDateTime.of(2025, 4, 1, 10, 0, 0));
      when(detail.waitingNo()).thenReturn(null);
      when(detail.paymentMethod()).thenReturn(CASH_ON_SITE);
      when(detail.paymentAmount()).thenReturn(50000);
      when(detail.paymentPendingAt()).thenReturn(LocalDateTime.of(2025, 4, 1, 10, 0, 0));
      when(detail.paymentApprovedAt()).thenReturn(null);
      return detail;
    }
  }
}