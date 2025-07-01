package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.db.entity.QReservationEntity.reservationEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolImageEntity.swimmingPoolImageEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolReviewEntity.swimmingPoolReviewEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.db.entity.AccountNo;
import com.project.swimcb.mypage.reservation.application.port.out.FindReservationDetailGateway;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Cancel;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Payment;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Refund;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Reservation;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Review;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.SwimmingClass;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.SwimmingPool;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Ticket;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindReservationDetailDataMapper implements FindReservationDetailGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public ReservationDetail findReservationDetail(long reservationId) {
    val result = queryFactory.select(constructor(QueryReservationDetail.class,
            swimmingPoolEntity.id,
            swimmingPoolEntity.name,
            swimmingPoolEntity.phone,
            swimmingPoolImageEntity.path,
            swimmingPoolEntity.accountNo,

            swimmingClassEntity.id,
            swimmingClassEntity.month,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,
            swimmingClassEntity.isCanceled,
            swimmingClassEntity.cancelReason,

            ticketEntity.id,
            ticketEntity.name,
            ticketEntity.price,

            reservationEntity.reservationStatus,
            reservationEntity.reservedAt,
            reservationEntity.paymentMethod,
            reservationEntity.paymentAmount,
            reservationEntity.paymentPendingAt,
            reservationEntity.paymentApprovedAt,
            reservationEntity.canceledAt,
            reservationEntity.refundAmount,
            reservationEntity.refundAccountNo,
            reservationEntity.refundBankName,
            reservationEntity.refundedAt,

            swimmingPoolReviewEntity.id
        ))
        .from(reservationEntity)
        .join(ticketEntity)
        .on(reservationEntity.ticketId.eq(ticketEntity.id))
        .join(swimmingClassEntity)
        .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
        .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
        .join(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
        .join(swimmingPoolEntity).on(swimmingClassEntity.swimmingPool.eq(swimmingPoolEntity))
        .join(swimmingPoolImageEntity)
        .on(swimmingPoolEntity.eq(swimmingPoolImageEntity.swimmingPool))
        .leftJoin(swimmingPoolReviewEntity)
        .on(swimmingPoolEntity.eq(swimmingPoolReviewEntity.swimmingPool))
        .where(
            reservationEntity.id.eq(reservationId),
            ticketEntity.targetType.eq(SWIMMING_CLASS)
        )
        .fetchFirst();

    if (result == null) {
      throw new NoSuchElementException("예약 상세 정보가 존재하지 않습니다 : " + reservationId);
    }

    if (result.reservationStatus != ReservationStatus.RESERVATION_PENDING) {
      return reservationDetail(reservationId, result, null);
    }

    // 대기 예약인 경우, 현재 대기 번호를 조회하여 설정
    val waitingNo = findCurrentWaitingNo(result.swimmingClassId(), result.reservedAt());

    return reservationDetail(reservationId, result, waitingNo.intValue());
  }

  private Long findCurrentWaitingNo(
      @NonNull Long swimmingClassId,
      @NonNull LocalDateTime reservedAt
  ) {
    val count = Optional.ofNullable(queryFactory.select(reservationEntity.id.count())
            .from(reservationEntity)
            .join(ticketEntity)
            .on(reservationEntity.ticketId.eq(ticketEntity.id))
            .join(swimmingClassEntity)
            .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
            .where(
                swimmingClassEntity.id.eq(swimmingClassId),
                ticketEntity.targetType.eq(SWIMMING_CLASS),
                reservationEntity.reservationStatus.eq(RESERVATION_PENDING),
                reservationEntity.reservedAt.before(reservedAt) // 현재 예약보다 이전에 예약된 대기 예약들만 카운트
            )
            .fetchOne())
        .orElse(0L);
    return count + 1;
  }

  private ReservationDetail reservationDetail(
      @NonNull Long reservationId,
      @NonNull QueryReservationDetail result,
      Integer waitingNo
  ) {
    return ReservationDetail.builder()
        .swimmingPool(
            SwimmingPool.builder()
                .id(result.swimmingPoolId())
                .name(result.swimmingPoolName())
                .phone(result.swimmingPoolPhone())
                .imagePath(result.swimmingPoolImagePath())
                .accountNo(result.accountNo())
                .build()
        )
        .swimmingClass(
            SwimmingClass.builder()
                .id(result.swimmingClassId())
                .month(result.month())
                .type(result.swimmingClassType())
                .subType(result.swimmingClassSubType())
                .daysOfWeek(ClassDayOfWeek.of(result.daysOfWeek()))
                .startTime(result.startTime())
                .endTime(result.endTime())
                .isCanceled(result.isCanceled())
                .cancelledReason(result.cancelledReason())
                .build()
        )
        .ticket(
            Ticket.builder()
                .id(result.ticketId())
                .name(result.ticketName())
                .price(result.ticketPrice())
                .build()
        )
        .reservation(
            Reservation.builder()
                .id(reservationId)
                .status(result.reservationStatus())
                .waitingNo(waitingNo)
                .reservedAt(result.reservedAt())
                .build()
        )
        .payment(
            Payment.builder()
                .method(result.paymentMethod())
                .amount(result.paymentAmount())
                .pendingAt(result.paymentPendingAt())
                .approvedAt(result.paymentApprovedAt())
                .build()
        )
        .cancel(cancel(result))
        .refund(refund(result))
        .review(review(result))
        .build();
  }

  private Cancel cancel(@NonNull QueryReservationDetail result) {
    if (result.reservationStatus() != ReservationStatus.RESERVATION_CANCELLED) {
      return null;
    }
    return Cancel.builder()
        .canceledAt(result.canceledAt())
        .build();
  }

  private Refund refund(@NonNull QueryReservationDetail result) {
    if (result.reservationStatus() != ReservationStatus.REFUND_COMPLETED) {
      return null;
    }
    return Refund.builder()
        .amount(result.refundAmount())
        .accountNo(result.refundAccountNo())
        .bankName(result.refundBankName())
        .refundedAt(result.refundedAt())
        .build();
  }

  private Review review(@NonNull QueryReservationDetail result) {
    if (result.reviewId() == null) {
      return null;
    }
    return Review.builder()
        .id(result.reviewId())
        .build();
  }

  @Builder
  public record QueryReservationDetail(
      long swimmingPoolId,
      String swimmingPoolName,
      String swimmingPoolPhone,
      String swimmingPoolImagePath,
      AccountNo accountNo,

      long swimmingClassId,
      int month,
      SwimmingClassTypeName swimmingClassType,
      String swimmingClassSubType,
      int daysOfWeek,
      LocalTime startTime,
      LocalTime endTime,
      boolean isCanceled,
      String cancelledReason,

      long ticketId,
      String ticketName,
      int ticketPrice,

      ReservationStatus reservationStatus,
      LocalDateTime reservedAt,
      PaymentMethod paymentMethod,
      int paymentAmount,
      LocalDateTime paymentPendingAt,
      LocalDateTime paymentApprovedAt,
      LocalDateTime canceledAt,
      Integer refundAmount,
      AccountNo refundAccountNo,
      String refundBankName,
      LocalDateTime refundedAt,

      Long reviewId
  ) {

    @QueryProjection
    public QueryReservationDetail {
    }

  }

}
