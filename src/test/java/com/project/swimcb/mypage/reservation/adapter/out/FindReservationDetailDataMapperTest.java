package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.swimcb.mypage.reservation.adapter.out.FindReservationDetailDataMapper.QueryReservationDetail;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
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

      // SwimmingClass 검증
      assertThat(result.swimmingClass().id()).isEqualTo(queryResult.swimmingClassId());
      assertThat(result.swimmingClass().type()).isEqualTo(queryResult.swimmingClassType());
      assertThat(result.swimmingClass().subType()).isEqualTo(queryResult.swimmingClassSubType());

      // Ticket 검증
      assertThat(result.ticket().id()).isEqualTo(queryResult.ticketId());
      assertThat(result.ticket().name()).isEqualTo(queryResult.ticketName());
      assertThat(result.ticket().price()).isEqualTo(queryResult.ticketPrice());

      // Reservation 검증
      assertThat(result.reservation().id()).isEqualTo(reservationId);
      assertThat(result.reservation().status()).isEqualTo(queryResult.reservationStatus());
      assertThat(result.reservation().reservedAt()).isEqualTo(queryResult.reservedAt());

      // Payment 검증
      assertThat(result.payment().method()).isEqualTo(queryResult.paymentMethod());
      assertThat(result.payment().amount()).isEqualTo(queryResult.paymentAmount());
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

    /**
     * QueryDSL 체인을 설정하는 메서드
     */
    private void setupQueryChain(QueryReservationDetail result) {
      when(queryFactory.select(any(Expression.class))).thenReturn(query);
      when(query.from(any(EntityPath.class))).thenReturn(query);
      when(query.join(any(EntityPath.class))).thenReturn(query);
      when(query.on(any(Predicate.class))).thenReturn(query);
      when(query.where(any(Predicate.class))).thenReturn(query);
      when(query.fetchFirst()).thenReturn(result);
    }
  }

  private static class TestQueryReservationDetailFactory {

    private static QueryReservationDetail create() {
      return new QueryReservationDetail(
          1L, "DUMMY_POOL_NAME", "DUMMY_IMAGE_PATH",
          2L, GROUP, "DUMMY_CLASS_SUB_TYPE",
          3L, "DUMMY_TICKET_NAME", 50000,
          PAYMENT_PENDING, LocalDateTime.of(2025, 4, 1, 10, 0, 0),
          CASH_ON_SITE, 50000
      );
    }
  }
}