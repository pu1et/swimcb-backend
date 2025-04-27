package com.project.swimcb.mypage.reservation.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.project.swimcb.mypage.reservation.adapter.out.QFindReservationDetailDataMapper_QueryReservationDetail is a Querydsl Projection type for QueryReservationDetail
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindReservationDetailDataMapper_QueryReservationDetail extends ConstructorExpression<FindReservationDetailDataMapper.QueryReservationDetail> {

    private static final long serialVersionUID = -2001120381L;

    public QFindReservationDetailDataMapper_QueryReservationDetail(com.querydsl.core.types.Expression<Long> swimmingPoolId, com.querydsl.core.types.Expression<String> swimmingPoolName, com.querydsl.core.types.Expression<String> swimmingPoolPhone, com.querydsl.core.types.Expression<String> swimmingPoolImagePath, com.querydsl.core.types.Expression<com.project.swimcb.bo.swimmingpool.domain.AccountNo> accountNo, com.querydsl.core.types.Expression<Long> swimmingClassId, com.querydsl.core.types.Expression<Integer> month, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName> swimmingClassType, com.querydsl.core.types.Expression<String> swimmingClassSubType, com.querydsl.core.types.Expression<Integer> daysOfWeek, com.querydsl.core.types.Expression<java.time.LocalTime> startTime, com.querydsl.core.types.Expression<java.time.LocalTime> endTime, com.querydsl.core.types.Expression<Long> ticketId, com.querydsl.core.types.Expression<String> ticketName, com.querydsl.core.types.Expression<Integer> ticketPrice, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.ReservationStatus> reservationStatus, com.querydsl.core.types.Expression<java.time.LocalDateTime> reservedAt, com.querydsl.core.types.Expression<Integer> waitingNo, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.PaymentMethod> paymentMethod, com.querydsl.core.types.Expression<Integer> paymentAmount, com.querydsl.core.types.Expression<java.time.LocalDateTime> paymentPendingAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> paymentApprovedAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> canceledAt, com.querydsl.core.types.Expression<Integer> refundAmount, com.querydsl.core.types.Expression<com.project.swimcb.bo.swimmingpool.domain.AccountNo> refundAccountNo, com.querydsl.core.types.Expression<String> refundBankName, com.querydsl.core.types.Expression<java.time.LocalDateTime> refundedAt, com.querydsl.core.types.Expression<Long> reviewId) {
        super(FindReservationDetailDataMapper.QueryReservationDetail.class, new Class<?>[]{long.class, String.class, String.class, String.class, com.project.swimcb.bo.swimmingpool.domain.AccountNo.class, long.class, int.class, com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.class, String.class, int.class, java.time.LocalTime.class, java.time.LocalTime.class, long.class, String.class, int.class, com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.class, java.time.LocalDateTime.class, int.class, com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.class, int.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, int.class, com.project.swimcb.bo.swimmingpool.domain.AccountNo.class, String.class, java.time.LocalDateTime.class, long.class}, swimmingPoolId, swimmingPoolName, swimmingPoolPhone, swimmingPoolImagePath, accountNo, swimmingClassId, month, swimmingClassType, swimmingClassSubType, daysOfWeek, startTime, endTime, ticketId, ticketName, ticketPrice, reservationStatus, reservedAt, waitingNo, paymentMethod, paymentAmount, paymentPendingAt, paymentApprovedAt, canceledAt, refundAmount, refundAccountNo, refundBankName, refundedAt, reviewId);
    }

}

