package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPoolImage.swimmingPoolImage;
import static com.project.swimcb.swimming_pool_review.domain.QSwimmingPoolReview.swimmingPoolReview;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.mypage.reservation.application.port.out.FindReservationsDsGateway;
import com.project.swimcb.mypage.reservation.domain.Reservation;
import com.project.swimcb.mypage.reservation.domain.Reservation.ReservationDetail;
import com.project.swimcb.mypage.reservation.domain.Reservation.Review;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingClass;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingPool;
import com.project.swimcb.mypage.reservation.domain.Reservation.Ticket;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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
            reservation.waitingNo,

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

   return new PageImpl<>(result, pageable, count);
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

      Long reviewId
  ) {

    @QueryProjection
    public QueryReservation {
    }
  }
}
