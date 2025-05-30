package com.project.swimcb.reservation.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.reservation.adapter.out.FindReservationDataMapper.QueryReservationInfo;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
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
class FindReservationDataMapperTest {

  @InjectMocks
  private FindReservationDataMapper mapper;

  @Mock
  private JPAQueryFactory queryFactory;

  @Mock
  private JPAQuery<QueryReservationInfo> query;

  @Nested
  @DisplayName("예약 조회 시")
  class FindReservationTest {

    @Test
    @DisplayName("유효한 예약 ID로 조회하면 예약 정보를 반환한다")
    void returnReservationInfoWithValidId() {
      // given
      val reservationId = 1L;
      val queryResult = TestQueryReservationInfoFactory.create();

      // QueryDSL 체인 설정
      setupQueryChain(queryResult);

      // when
      val result = mapper.findReservation(reservationId);

      // then
      assertThat(result).isNotNull();

      // SwimmingPool 검증
      assertThat(result.swimmingPool().id()).isEqualTo(queryResult.swimmingPoolId());
      assertThat(result.swimmingPool().name()).isEqualTo(queryResult.swimmingPoolName());
      assertThat(result.swimmingPool().accountNo()).isEqualTo(queryResult.accountNo());

      // SwimmingClass 검증
      assertThat(result.swimmingClass().id()).isEqualTo(queryResult.swimmingClassId());
      assertThat(result.swimmingClass().type()).isEqualTo(queryResult.swimmingClassType());
      assertThat(result.swimmingClass().daysOfWeek()).isEqualTo(queryResult.daysOfWeek());
      assertThat(result.swimmingClass().startTime()).isEqualTo(queryResult.startTime());

      // Ticket 검증
      assertThat(result.ticket().id()).isEqualTo(queryResult.ticketId());

      // Reservation 검증
      assertThat(result.reservation().id()).isEqualTo(reservationId);
      assertThat(result.reservation().waitingNo()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약 정보가 조회되지 않을 때 예외가 발생한다")
    void throwExceptionWhenReservationNotFound() {
      // given
      val reservationId = 999L;

      // QueryDSL 체인 설정 - 결과 없음
      setupQueryChain(null);

      // when
      // then
      assertThatThrownBy(() -> mapper.findReservation(reservationId))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessageContaining("예약 정보가 존재하지 않습니다");
    }

    /**
     * QueryDSL 체인을 설정하는 메서드 테스트마다 다른 결과값을 반환할 수 있도록 메서드로 분리
     */
    private void setupQueryChain(FindReservationDataMapper.QueryReservationInfo result) {
      when(queryFactory.select(any(Expression.class))).thenReturn(query);
      when(query.from(any(EntityPath.class))).thenReturn(query);
      when(query.join(any(EntityPath.class))).thenReturn(query);
      when(query.on(any(Predicate.class))).thenReturn(query);
      when(query.where(any(Predicate.class))).thenReturn(query);
      when(query.fetchFirst()).thenReturn(result);
    }
  }

  private static class TestQueryReservationInfoFactory {

    private static QueryReservationInfo create() {
      return new QueryReservationInfo(
          1L,
          "DUMMY_POOL_NAME",
          AccountNo.of("DUMMY_ACCOUNT_NO"),

          2L,
          5,
          GROUP,
          "DUMMY_CLASS_SUB_TYPE",
          1,
          LocalTime.of(10, 0),
          LocalTime.of(11, 0),
          10,
          11,

          3L,
          "DUMMY_TICKET_NAME",
          50000,

          LocalDateTime.of(2025, 4, 1, 10, 0, 0),
          ReservationStatus.RESERVATION_PENDING,
          CASH_ON_SITE
      );
    }
  }
}
