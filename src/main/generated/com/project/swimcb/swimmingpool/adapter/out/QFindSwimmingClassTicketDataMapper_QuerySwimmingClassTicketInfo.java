package com.project.swimcb.swimmingpool.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.project.swimcb.swimmingpool.adapter.out.QFindSwimmingClassTicketDataMapper_QuerySwimmingClassTicketInfo is a Querydsl Projection type for QuerySwimmingClassTicketInfo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindSwimmingClassTicketDataMapper_QuerySwimmingClassTicketInfo extends ConstructorExpression<FindSwimmingClassTicketDataMapper.QuerySwimmingClassTicketInfo> {

    private static final long serialVersionUID = -1880801423L;

    public QFindSwimmingClassTicketDataMapper_QuerySwimmingClassTicketInfo(com.querydsl.core.types.Expression<Long> swimmingClassId, com.querydsl.core.types.Expression<Integer> month, com.querydsl.core.types.Expression<Integer> reservationLimitCount, com.querydsl.core.types.Expression<Integer> reservedCount, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName> clssType, com.querydsl.core.types.Expression<String> classSubType, com.querydsl.core.types.Expression<Integer> daysOfWeek, com.querydsl.core.types.Expression<java.time.LocalTime> startTime, com.querydsl.core.types.Expression<java.time.LocalTime> endTime, com.querydsl.core.types.Expression<String> ticketName, com.querydsl.core.types.Expression<Integer> ticketPrice) {
        super(FindSwimmingClassTicketDataMapper.QuerySwimmingClassTicketInfo.class, new Class<?>[]{long.class, int.class, int.class, int.class, com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.class, String.class, int.class, java.time.LocalTime.class, java.time.LocalTime.class, String.class, int.class}, swimmingClassId, month, reservationLimitCount, reservedCount, clssType, classSubType, daysOfWeek, startTime, endTime, ticketName, ticketPrice);
    }

}

