package com.project.swimcb.mypage.reservation.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.project.swimcb.mypage.reservation.adapter.out.QFindReservationsDataMapper_QueryReservation is a Querydsl Projection type for QueryReservation
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindReservationsDataMapper_QueryReservation extends ConstructorExpression<FindReservationsDataMapper.QueryReservation> {

    private static final long serialVersionUID = 1786140760L;

    public QFindReservationsDataMapper_QueryReservation(com.querydsl.core.types.Expression<Long> swimmingPoolId, com.querydsl.core.types.Expression<String> swimmingPoolName, com.querydsl.core.types.Expression<String> swimmingPoolImagePath, com.querydsl.core.types.Expression<Long> swimmingClassId, com.querydsl.core.types.Expression<Integer> month, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName> swimmingClassType, com.querydsl.core.types.Expression<String> swimmingClassSubType, com.querydsl.core.types.Expression<Integer> daysOfWeek, com.querydsl.core.types.Expression<java.time.LocalTime> startTime, com.querydsl.core.types.Expression<java.time.LocalTime> endTime, com.querydsl.core.types.Expression<Long> ticketId, com.querydsl.core.types.Expression<String> ticketName, com.querydsl.core.types.Expression<Integer> ticketPrice, com.querydsl.core.types.Expression<Long> reservationId, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.TicketType> ticketType, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.ReservationStatus> reservationStatus, com.querydsl.core.types.Expression<java.time.LocalDateTime> reservedAt, com.querydsl.core.types.Expression<Integer> waitingNo, com.querydsl.core.types.Expression<Long> reviewId) {
        super(FindReservationsDataMapper.QueryReservation.class, new Class<?>[]{long.class, String.class, String.class, long.class, int.class, com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.class, String.class, int.class, java.time.LocalTime.class, java.time.LocalTime.class, long.class, String.class, int.class, long.class, com.project.swimcb.swimmingpool.domain.enums.TicketType.class, com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.class, java.time.LocalDateTime.class, int.class, long.class}, swimmingPoolId, swimmingPoolName, swimmingPoolImagePath, swimmingClassId, month, swimmingClassType, swimmingClassSubType, daysOfWeek, startTime, endTime, ticketId, ticketName, ticketPrice, reservationId, ticketType, reservationStatus, reservedAt, waitingNo, reviewId);
    }

}

