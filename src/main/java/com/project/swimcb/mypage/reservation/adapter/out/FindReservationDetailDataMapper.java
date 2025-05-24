package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPoolImage.swimmingPoolImage;
import static com.project.swimcb.swimming_pool_review.domain.QSwimmingPoolReview.swimmingPoolReview;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.FindReservationsDataMapper.WaitingReservation;
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
            swimmingPool.id,
            swimmingPool.name,
            swimmingPool.phone,
            swimmingPoolImage.path,
            swimmingPool.accountNo,

            swimmingClass.id,
            swimmingClass.month,
            swimmingClassType.name,
            swimmingClassSubType.name,
            swimmingClass.daysOfWeek,
            swimmingClass.startTime,
            swimmingClass.endTime,
            swimmingClass.isCanceled,
            swimmingClass.cancelReason,

            swimmingClassTicket.id,
            swimmingClassTicket.name,
            swimmingClassTicket.price,

            reservation.reservationStatus,
            reservation.reservedAt,
            reservation.waitingNo,
            reservation.paymentMethod,
            reservation.paymentAmount,
            reservation.paymentPendingAt,
            reservation.paymentApprovedAt,
            reservation.canceledAt,
            reservation.refundAmount,
            reservation.refundAccountNo,
            reservation.refundBankName,
            reservation.refundedAt,

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
            reservation.id.eq(reservationId)
        )
        .fetchFirst();

    if (result == null) {
      throw new NoSuchElementException("예약 상세 정보가 존재하지 않습니다 : " + reservationId);
    }

    if (result.reservationStatus != ReservationStatus.RESERVATION_PENDING) {
      return reservationDetail(reservationId, result, null);
    }

    // 대기 예약인 경우, 현재 대기 번호를 조회하여 설정
    val waitingNo = findCurrentWaitingNo(result.swimmingClassId(), result.waitingNo());

    return reservationDetail(reservationId, result, waitingNo.intValue());
  }

  private Long findCurrentWaitingNo(
      @NonNull Long swimmingClassId,
      @NonNull Integer waitingNo
  ) {
    val count = Optional.ofNullable(queryFactory.select(reservation.id.count())
            .from(reservation)
            .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
            .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
            .where(
                swimmingClass.id.eq(swimmingClassId),
                reservation.reservationStatus.eq(RESERVATION_PENDING),
                reservation.waitingNo.lt(waitingNo) // 현재 예약의 waitingNo보다 작은 예약들만 조회
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
                .waitingNo(result.waitingNo())
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
      Integer waitingNo,
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
