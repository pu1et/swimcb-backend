package com.project.swimcb.swimmingpool.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.project.swimcb.swimmingpool.adapter.out.QFindSwimmingClassesDataMapper_SwimmingPoolWithClass is a Querydsl Projection type for SwimmingPoolWithClass
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindSwimmingClassesDataMapper_SwimmingPoolWithClass extends ConstructorExpression<FindSwimmingClassesDataMapper.SwimmingPoolWithClass> {

    private static final long serialVersionUID = -1232531785L;

    public QFindSwimmingClassesDataMapper_SwimmingPoolWithClass(com.querydsl.core.types.Expression<Long> swimmingPoolId, com.querydsl.core.types.Expression<String> imageUrl, com.querydsl.core.types.Expression<Boolean> isFavorite, com.querydsl.core.types.Expression<Double> distance, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> address, com.querydsl.core.types.Expression<Double> rating, com.querydsl.core.types.Expression<Long> reviewCount) {
        super(FindSwimmingClassesDataMapper.SwimmingPoolWithClass.class, new Class<?>[]{long.class, String.class, boolean.class, double.class, String.class, String.class, double.class, long.class}, swimmingPoolId, imageUrl, isFavorite, distance, name, address, rating, reviewCount);
    }

}

