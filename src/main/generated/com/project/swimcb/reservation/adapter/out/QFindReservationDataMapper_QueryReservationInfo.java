package com.project.swimcb.reservation.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.project.swimcb.reservation.adapter.out.QFindReservationDataMapper_QueryReservationInfo is a Querydsl Projection type for QueryReservationInfo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindReservationDataMapper_QueryReservationInfo extends ConstructorExpression<FindReservationDataMapper.QueryReservationInfo> {

    private static final long serialVersionUID = 450460872L;

    public QFindReservationDataMapper_QueryReservationInfo(com.querydsl.core.types.Expression<Long> swimmingPoolId, com.querydsl.core.types.Expression<String> swimmingPoolName, com.querydsl.core.types.Expression<com.project.swimcb.bo.swimmingpool.domain.AccountNo> accountNo, com.querydsl.core.types.Expression<Long> swimmingClassId, com.querydsl.core.types.Expression<Integer> month, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName> swimmingClassType, com.querydsl.core.types.Expression<String> swimmingClassSubType, com.querydsl.core.types.Expression<Integer> daysOfWeek, com.querydsl.core.types.Expression<java.time.LocalTime> startTime, com.querydsl.core.types.Expression<java.time.LocalTime> endTime, com.querydsl.core.types.Expression<Long> ticketId, com.querydsl.core.types.Expression<String> ticketName, com.querydsl.core.types.Expression<Integer> ticketPrice, com.querydsl.core.types.Expression<java.time.LocalDateTime> reservedAt, com.querydsl.core.types.Expression<Integer> waitingNo, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.PaymentMethod> paymentMethod) {
        super(FindReservationDataMapper.QueryReservationInfo.class, new Class<?>[]{long.class, String.class, com.project.swimcb.bo.swimmingpool.domain.AccountNo.class, long.class, int.class, com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.class, String.class, int.class, java.time.LocalTime.class, java.time.LocalTime.class, long.class, String.class, int.class, java.time.LocalDateTime.class, int.class, com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.class}, swimmingPoolId, swimmingPoolName, accountNo, swimmingClassId, month, swimmingClassType, swimmingClassSubType, daysOfWeek, startTime, endTime, ticketId, ticketName, ticketPrice, reservedAt, waitingNo, paymentMethod);
    }

}

