package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPoolImage.swimmingPoolImage;
import static com.project.swimcb.swimming_pool_review.domain.QSwimmingPoolReview.swimmingPoolReview;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.*;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.mypage.reservation.application.port.out.FindReservationsDsGateway;
import com.project.swimcb.mypage.reservation.domain.Reservation;
import com.project.swimcb.mypage.reservation.domain.Reservation.Payment;
import com.project.swimcb.mypage.reservation.domain.Reservation.ReservationDetail;
import com.project.swimcb.mypage.reservation.domain.Reservation.Review;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingClass;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingPool;
import com.project.swimcb.mypage.reservation.domain.Reservation.Ticket;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindReservationsDataMapper implements FindReservationsDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Reservation> findReservations(long memberId, @NonNull Pageable pageable) {
    val result = queryFactory.select(constructor(QueryReservation.class,
            swimmingPool.id,
            swimmingPool.name,
            swimmingPoolImage.path,

            swimmingClass.id,
            swimmingClass.month,
            swimmingClassType.name,
            swimmingClassSubType.name,
            swimmingClass.daysOfWeek,
            swimmingClass.startTime,
            swimmingClass.endTime,
            swimmingClass.isCanceled,

            swimmingClassTicket.id,
            swimmingClassTicket.name,
            swimmingClassTicket.price,

            reservation.id,
            reservation.ticketType,
            reservation.reservationStatus,
            reservation.reservedAt,

            reservation.paymentMethod,
            reservation.paymentPendingAt,

            swimmingPoolReview.id
        ))
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
        .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
        .join(swimmingPool).on(swimmingClass.swimmingPool.eq(swimmingPool))
        .join(swimmingPoolImage).on(swimmingPool.eq(swimmingPoolImage.swimmingPool))
        .leftJoin(swimmingPoolReview).on(swimmingPool.eq(swimmingPoolReview.swimmingPool))
        .where(
            reservation.member.id.eq(memberId)
        )
        .orderBy(reservation.reservedAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch()
        .stream()
        .map(this::reservation)
        .toList();

    val waitingNoMap = getCurrentWaitingNoByReservation(result);

    // 예약 상태가 "예약대기"인 경우에만 waitingNo를 설정
    val resultWithWaitingNo = result.stream()
        .map(i -> {
          if (i.reservationDetail().status() != RESERVATION_PENDING) {
            return i;
          }
          return i.withReservationDetail(
              i.reservationDetail().withWaitingNo(waitingNoMap.get(i.reservationDetail().id()))
          );
        })
        .toList();

    val count = queryFactory.select(swimmingPool.id.count())
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
        .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
        .join(swimmingPool).on(swimmingClass.swimmingPool.eq(swimmingPool))
        .join(swimmingPoolImage).on(swimmingPool.eq(swimmingPoolImage.swimmingPool))
        .where(
            reservation.member.id.eq(memberId)
        )
        .fetchOne();

    return new PageImpl<>(resultWithWaitingNo, pageable, count);
  }

  Map<Long, Integer> getCurrentWaitingNoByReservation(@NonNull List<Reservation> reservations) {
    // 1. 예약 상태가 PENDING인 예약만 필터링
    val resrvationPendingReservations = reservations.stream()
        .filter(r -> r.reservationDetail().status() == RESERVATION_PENDING)
        .toList();

    if (resrvationPendingReservations.isEmpty()) return Map.of();

    // 2. 수영 클래스 ID 목록 추출
    val classIds = resrvationPendingReservations.stream()
        .map(r -> r.swimmingClass().id())
        .toList();

    // 3. 해당 클래스들의 전체 대기 예약 정보 조회
    val groupedByClassId = findRelatedReservationPendingReservations(classIds).stream()
        .collect(Collectors.groupingBy(WaitingReservation::swimmingClassId));

    // 4. 각 예약에 대해 대기 순번 계산 후 Map으로 반환
    return resrvationPendingReservations.stream()
        .collect(Collectors.toMap(
            i -> i.reservationDetail().id(),
            i -> {
              val classId = i.swimmingClass().id();
              return Optional.ofNullable(groupedByClassId.get(classId))
                  .orElse(List.of())
                  .stream()
                  .filter(wr -> wr.reservedAt().isBefore(i.reservationDetail().reservedAt()))
                  .mapToInt(x -> 1)
                  .sum() + 1;
            }
        ));
  }

  List<WaitingReservation> findRelatedReservationPendingReservations(@NonNull List<Long> swimmingClassIds) {
    return queryFactory.select(constructor(WaitingReservation.class,
            reservation.id,
            reservation.reservedAt,
            swimmingClass.id
        ))
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .where(
            swimmingClass.id.in(swimmingClassIds),
            reservation.reservationStatus.eq(RESERVATION_PENDING)
        )
        .fetch();
  }

  private Reservation reservation(@NonNull QueryReservation i) {
    return Reservation.builder()
        .swimmingPool(
            SwimmingPool.builder()
                .id(i.swimmingPoolId())
                .name(i.swimmingPoolName())
                .imagePath(i.swimmingPoolImagePath())
                .build()
        )
        .swimmingClass(
            SwimmingClass.builder()
                .id(i.swimmingClassId())
                .month(i.month())
                .type(i.swimmingClassType())
                .subType(i.swimmingClassSubType())
                .daysOfWeek(ClassDayOfWeek.of(i.daysOfWeek()))
                .startTime(i.startTime())
                .endTime(i.endTime())
                .isCanceled(i.isCanceled())
                .build()
        )
        .ticket(
            Ticket.builder()
                .id(i.ticketId())
                .name(i.ticketName())
                .price(i.ticketPrice())
                .build()
        )
        .reservationDetail(
            ReservationDetail.builder()
                .id(i.reservationId())
                .ticketType(i.ticketType())
                .status(i.reservationStatus())
                .reservedAt(i.reservedAt())
                .waitingNo(i.waitingNo())
                .build()
        )
        .payment(
            Payment.builder()
                .method(i.paymentMethod())
                .pendingAt(i.paymentPendingAt())
                .build()
        )
        .review(
            Optional.ofNullable(i.reviewId())
                .map(j -> Review.builder()
                    .id(j)
                    .build())
                .orElse(null)
        )
        .build();
  }

  @Builder
  protected record QueryReservation(
      long swimmingPoolId,
      String swimmingPoolName,
      String swimmingPoolImagePath,

      long swimmingClassId,
      int month,
      SwimmingClassTypeName swimmingClassType,
      String swimmingClassSubType,
      int daysOfWeek,
      LocalTime startTime,
      LocalTime endTime,
      boolean isCanceled,

      long ticketId,
      String ticketName,
      int ticketPrice,

      long reservationId,
      TicketType ticketType,
      ReservationStatus reservationStatus,
      LocalDateTime reservedAt,
      Integer waitingNo,

      PaymentMethod paymentMethod,
      LocalDateTime paymentPendingAt,

      Long reviewId
  ) {

    @QueryProjection
    public QueryReservation {
    }

  }

  @Builder
  protected record WaitingReservation(
      long reservationId,
      LocalDateTime reservedAt,
      long swimmingClassId
  ) {

    @QueryProjection
    public WaitingReservation {
    }
  }

}
