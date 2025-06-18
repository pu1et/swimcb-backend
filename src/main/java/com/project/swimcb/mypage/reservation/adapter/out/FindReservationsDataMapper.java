package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.db.entity.QReservationEntity.reservationEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTicketEntity.swimmingClassTicketEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolImageEntity.swimmingPoolImageEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolReviewEntity.swimmingPoolReviewEntity;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
            swimmingPoolEntity.id,
            swimmingPoolEntity.name,
            swimmingPoolImageEntity.path,

            swimmingClassEntity.id,
            swimmingClassEntity.month,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,
            swimmingClassEntity.isCanceled,

            swimmingClassTicketEntity.id,
            swimmingClassTicketEntity.name,
            swimmingClassTicketEntity.price,

            reservationEntity.id,
            reservationEntity.ticketType,
            reservationEntity.reservationStatus,
            reservationEntity.reservedAt,

            reservationEntity.paymentMethod,
            reservationEntity.paymentPendingAt,

            swimmingPoolReviewEntity.id
        ))
        .from(reservationEntity)
        .join(swimmingClassTicketEntity)
        .on(reservationEntity.ticketId.eq(swimmingClassTicketEntity.id))
        .join(swimmingClassEntity)
        .on(swimmingClassTicketEntity.swimmingClass.eq(swimmingClassEntity))
        .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
        .join(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
        .join(swimmingPoolEntity).on(swimmingClassEntity.swimmingPool.eq(swimmingPoolEntity))
        .join(swimmingPoolImageEntity)
        .on(swimmingPoolEntity.eq(swimmingPoolImageEntity.swimmingPool))
        .leftJoin(swimmingPoolReviewEntity)
        .on(swimmingPoolEntity.eq(swimmingPoolReviewEntity.swimmingPool))
        .where(
            reservationEntity.member.id.eq(memberId)
        )
        .orderBy(reservationEntity.reservedAt.desc())
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

    val count = queryFactory.select(swimmingPoolEntity.id.count())
        .from(reservationEntity)
        .join(swimmingClassTicketEntity)
        .on(reservationEntity.ticketId.eq(swimmingClassTicketEntity.id))
        .join(swimmingClassEntity)
        .on(swimmingClassTicketEntity.swimmingClass.eq(swimmingClassEntity))
        .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
        .join(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
        .join(swimmingPoolEntity).on(swimmingClassEntity.swimmingPool.eq(swimmingPoolEntity))
        .join(swimmingPoolImageEntity)
        .on(swimmingPoolEntity.eq(swimmingPoolImageEntity.swimmingPool))
        .where(
            reservationEntity.member.id.eq(memberId)
        )
        .fetchOne();

    return new PageImpl<>(resultWithWaitingNo, pageable, count);
  }

  Map<Long, Integer> getCurrentWaitingNoByReservation(@NonNull List<Reservation> reservations) {
    // 1. 예약 상태가 PENDING인 예약만 필터링
    val resrvationPendingReservations = reservations.stream()
        .filter(r -> r.reservationDetail().status() == RESERVATION_PENDING)
        .toList();

    if (resrvationPendingReservations.isEmpty()) {
      return Map.of();
    }

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

  List<WaitingReservation> findRelatedReservationPendingReservations(
      @NonNull List<Long> swimmingClassIds) {
    return queryFactory.select(constructor(WaitingReservation.class,
            reservationEntity.id,
            reservationEntity.reservedAt,
            swimmingClassEntity.id
        ))
        .from(reservationEntity)
        .join(swimmingClassTicketEntity)
        .on(reservationEntity.ticketId.eq(swimmingClassTicketEntity.id))
        .join(swimmingClassEntity)
        .on(swimmingClassTicketEntity.swimmingClass.eq(swimmingClassEntity))
        .where(
            swimmingClassEntity.id.in(swimmingClassIds),
            reservationEntity.reservationStatus.eq(RESERVATION_PENDING)
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
