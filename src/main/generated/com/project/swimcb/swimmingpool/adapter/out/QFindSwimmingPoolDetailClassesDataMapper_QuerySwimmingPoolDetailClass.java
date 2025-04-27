package com.project.swimcb.swimmingpool.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.project.swimcb.swimmingpool.adapter.out.QFindSwimmingPoolDetailClassesDataMapper_QuerySwimmingPoolDetailClass is a Querydsl Projection type for QuerySwimmingPoolDetailClass
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindSwimmingPoolDetailClassesDataMapper_QuerySwimmingPoolDetailClass extends ConstructorExpression<FindSwimmingPoolDetailClassesDataMapper.QuerySwimmingPoolDetailClass> {

    private static final long serialVersionUID = 1016473941L;

    public QFindSwimmingPoolDetailClassesDataMapper_QuerySwimmingPoolDetailClass(com.querydsl.core.types.Expression<Long> swimmingClassId, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName> typeName, com.querydsl.core.types.Expression<String> subTypeName, com.querydsl.core.types.Expression<Integer> daysOfWeek, com.querydsl.core.types.Expression<java.time.LocalTime> startTime, com.querydsl.core.types.Expression<java.time.LocalTime> endTime, com.querydsl.core.types.Expression<Integer> minimumPrice, com.querydsl.core.types.Expression<Boolean> isFavorite, com.querydsl.core.types.Expression<Integer> reservationLimitCount, com.querydsl.core.types.Expression<Integer> reservedCount, com.querydsl.core.types.Expression<Long> ticketId, com.querydsl.core.types.Expression<String> ticketName, com.querydsl.core.types.Expression<Integer> ticketPrice) {
        super(FindSwimmingPoolDetailClassesDataMapper.QuerySwimmingPoolDetailClass.class, new Class<?>[]{long.class, com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.class, String.class, int.class, java.time.LocalTime.class, java.time.LocalTime.class, int.class, boolean.class, int.class, int.class, long.class, String.class, int.class}, swimmingClassId, typeName, subTypeName, daysOfWeek, startTime, endTime, minimumPrice, isFavorite, reservationLimitCount, reservedCount, ticketId, ticketName, ticketPrice);
    }

}

