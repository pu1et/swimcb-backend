package com.project.swimcb.mypage.reservation.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.project.swimcb.mypage.reservation.adapter.out.FindReservationsDataMapper.QueryReservation;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FindReservationsDataMapperTest {

  @InjectMocks
  private FindReservationsDataMapper findReservationsDataMapper;

  @Mock
  private JPAQueryFactory queryFactory;

  private JPAQuery<QueryReservation> resultQuery;
  private JPAQuery<Long> countQuery;

  private long memberId = 1L;
  private Pageable pageable;

  @BeforeEach
  void setUp() {
    pageable = PageRequest.of(0, 10);

    setupQueryChain();
  }

  @Test
  @DisplayName("예약 목록 조회 시 정상적으로 매핑되어야 한다")
  void shouldMapCorrectlyWhenFetchingReservations() {
    // given
    val queryResult = TestQueryReservationFactory.create();
    val reviewId = 5L;

    when(queryResult.reviewId()).thenReturn(reviewId);

    when(resultQuery.fetch()).thenReturn(List.of(queryResult));
    when(countQuery.fetchOne()).thenReturn(1L);

    // when
    val result = findReservationsDataMapper.findReservations(memberId, pageable);

    // then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);

    val reservation = result.getContent().get(0);

    // 수영장 정보 확인
    assertThat(reservation.swimmingPool().id()).isEqualTo(queryResult.swimmingPoolId());
    assertThat(reservation.swimmingPool().name()).isEqualTo(queryResult.swimmingPoolName());
    assertThat(reservation.swimmingPool().imagePath()).isEqualTo(
        queryResult.swimmingPoolImagePath());

    // 수영 클래스 정보 확인
    assertThat(reservation.swimmingClass().id()).isEqualTo(queryResult.swimmingClassId());
    assertThat(reservation.swimmingClass().month()).isEqualTo(queryResult.month());
    assertThat(reservation.swimmingClass().type()).isEqualTo(queryResult.swimmingClassType());
    assertThat(reservation.swimmingClass().subType()).isEqualTo(queryResult.swimmingClassSubType());
    assertThat(reservation.swimmingClass().startTime()).isEqualTo(queryResult.startTime());
    assertThat(reservation.swimmingClass().endTime()).isEqualTo(queryResult.endTime());

    // 티켓 정보 확인
    assertThat(reservation.ticket().id()).isEqualTo(queryResult.ticketId());
    assertThat(reservation.ticket().name()).isEqualTo(queryResult.ticketName());
    assertThat(reservation.ticket().price()).isEqualTo(queryResult.ticketPrice());

    // 예약 상세 정보 확인
    assertThat(reservation.reservationDetail().id()).isEqualTo(queryResult.reservationId());
    assertThat(reservation.reservationDetail().ticketType()).isEqualTo(queryResult.ticketType());
    assertThat(reservation.reservationDetail().status()).isEqualTo(queryResult.reservationStatus());
    assertThat(reservation.reservationDetail().reservedAt()).isEqualTo(queryResult.reservedAt());
    assertThat(reservation.reservationDetail().waitingNo()).isEqualTo(queryResult.waitingNo());

    // 리뷰 정보 확인
    assertThat(reservation.review().id()).isEqualTo(queryResult.reviewId());
  }

  @Test
  @DisplayName("리뷰가 없는 경우에도 정상적으로 매핑되어야 한다")
  void shouldMapCorrectlyWhenReviewIsNull() {
    // given
    val queryResult = TestQueryReservationFactory.create();

    when(queryResult.reviewId()).thenReturn(null);

    when(resultQuery.fetch()).thenReturn(List.of(queryResult));
    when(countQuery.fetchOne()).thenReturn(1L);

    // when
    val result = findReservationsDataMapper.findReservations(memberId, pageable);

    // then
    assertThat(result)
        .isNotNull()
        .hasSize(1);

    val reservation = result.getContent().get(0);

    assertThat(reservation.review()).isNull();
  }

  @Test
  @DisplayName("예약 내역이 없는 경우 빈 페이지가 반환되어야 한다")
  void shouldReturnEmptyPageWhenNoReservations() {
    // given
    when(resultQuery.fetch()).thenReturn(List.of());
    when(countQuery.fetchOne()).thenReturn(0L);

    // when
    val result = findReservationsDataMapper.findReservations(memberId, pageable);

    // then
    assertThat(result)
        .isNotNull()
        .isEmpty();
    assertThat(result.getTotalElements()).isZero();
    assertThat(result.getTotalPages()).isZero();
  }

  /**
   * QueryDSL 체인을 설정하는 메서드
   */
  @SuppressWarnings("unchecked")
  private void setupQueryChain() {
    resultQuery = mock(JPAQuery.class);
    countQuery = mock(JPAQuery.class);

    when(queryFactory.select(any(Expression.class))).thenReturn(resultQuery);
    when(resultQuery.from(any(EntityPath.class))).thenReturn(resultQuery);
    when(resultQuery.join(any(EntityPath.class))).thenReturn(resultQuery);
    when(resultQuery.leftJoin(any(EntityPath.class))).thenReturn(resultQuery);
    when(resultQuery.on(any(Predicate.class))).thenReturn(resultQuery);
    when(resultQuery.where(any(Predicate.class))).thenReturn(resultQuery);
    when(resultQuery.orderBy(any(OrderSpecifier.class))).thenReturn(resultQuery);
    when(resultQuery.offset(any(Long.class))).thenReturn(resultQuery);
    when(resultQuery.limit(any(Long.class))).thenReturn(resultQuery);

    lenient().when(queryFactory.select(any(NumberExpression.class))).thenReturn(countQuery);
    lenient().when(countQuery.from(any(EntityPath.class))).thenReturn(countQuery);
    lenient().when(countQuery.join(any(EntityPath.class))).thenReturn(countQuery);
    lenient().when(countQuery.where(any(Predicate.class))).thenReturn(countQuery);
    lenient().when(countQuery.on(any(Predicate.class))).thenReturn(countQuery);
  }

  private static class TestQueryReservationFactory {

    public static QueryReservation create() {
      val queryReservation = mock(QueryReservation.class);

      when(queryReservation.swimmingPoolId()).thenReturn(1L);
      when(queryReservation.swimmingPoolName()).thenReturn("DUMMY_POOL_NAME");
      when(queryReservation.swimmingPoolImagePath()).thenReturn("DUMMY_POOL_IMAGE_PATH");
      when(queryReservation.swimmingClassId()).thenReturn(2L);
      when(queryReservation.month()).thenReturn(4);
      when(queryReservation.swimmingClassType()).thenReturn(SwimmingClassTypeName.GROUP);
      when(queryReservation.swimmingClassSubType()).thenReturn("DUMMY_SUB_TYPE");
      when(queryReservation.daysOfWeek()).thenReturn(0b1010100); // 월,수,금
      when(queryReservation.startTime()).thenReturn(LocalTime.of(10, 0));
      when(queryReservation.endTime()).thenReturn(LocalTime.of(11, 0));
      when(queryReservation.ticketId()).thenReturn(3L);
      when(queryReservation.ticketName()).thenReturn("DUMMY_TICKET_NAME");
      when(queryReservation.ticketPrice()).thenReturn(10000);
      when(queryReservation.reservationId()).thenReturn(4L);
      when(queryReservation.ticketType()).thenReturn(TicketType.SWIMMING_CLASS);
      when(queryReservation.reservationStatus()).thenReturn(ReservationStatus.PAYMENT_PENDING);
      when(queryReservation.reservedAt()).thenReturn(LocalDateTime.of(2023, 4, 1, 9, 0));
      when(queryReservation.waitingNo()).thenReturn(null);

      return queryReservation;
    }
  }
}