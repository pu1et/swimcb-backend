package com.project.swimcb.bo.reservation.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.project.swimcb.bo.reservation.adapter.out.QFindBoReservationsDataMapper_QueryReservation is a Querydsl Projection type for QueryReservation
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindBoReservationsDataMapper_QueryReservation extends ConstructorExpression<FindBoReservationsDataMapper.QueryReservation> {

    private static final long serialVersionUID = 1793040477L;

    public QFindBoReservationsDataMapper_QueryReservation(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<java.time.LocalDate> birthDate, com.querydsl.core.types.Expression<Long> swimmingClassId, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName> swimmingClassType, com.querydsl.core.types.Expression<String> swimmingClassSubType, com.querydsl.core.types.Expression<Integer> daysOfWeek, com.querydsl.core.types.Expression<java.time.LocalTime> startTime, com.querydsl.core.types.Expression<java.time.LocalTime> endTime, com.querydsl.core.types.Expression<Long> reservationId, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.TicketType> ticketType, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.ReservationStatus> reservationStatus, com.querydsl.core.types.Expression<java.time.LocalDateTime> reservedAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> paymentPendingAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> paymentCompletedAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> canceledAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> refundedAt, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.PaymentMethod> paymentMethod, com.querydsl.core.types.Expression<Integer> amount, com.querydsl.core.types.Expression<Integer> refundAmount, com.querydsl.core.types.Expression<com.project.swimcb.bo.swimmingpool.domain.AccountNo> refundAccountNo, com.querydsl.core.types.Expression<String> refundBankName) {
        super(FindBoReservationsDataMapper.QueryReservation.class, new Class<?>[]{long.class, String.class, java.time.LocalDate.class, long.class, com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.class, String.class, int.class, java.time.LocalTime.class, java.time.LocalTime.class, long.class, com.project.swimcb.swimmingpool.domain.enums.TicketType.class, com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.class, int.class, int.class, com.project.swimcb.bo.swimmingpool.domain.AccountNo.class, String.class}, memberId, name, birthDate, swimmingClassId, swimmingClassType, swimmingClassSubType, daysOfWeek, startTime, endTime, reservationId, ticketType, reservationStatus, reservedAt, paymentPendingAt, paymentCompletedAt, canceledAt, refundedAt, paymentMethod, amount, refundAmount, refundAccountNo, refundBankName);
    }

}

