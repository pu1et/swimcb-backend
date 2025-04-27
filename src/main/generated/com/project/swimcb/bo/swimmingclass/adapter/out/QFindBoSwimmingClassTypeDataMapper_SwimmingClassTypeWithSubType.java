package com.project.swimcb.bo.swimmingclass.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.project.swimcb.bo.swimmingclass.adapter.out.QFindBoSwimmingClassTypeDataMapper_SwimmingClassTypeWithSubType is a Querydsl Projection type for SwimmingClassTypeWithSubType
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindBoSwimmingClassTypeDataMapper_SwimmingClassTypeWithSubType extends ConstructorExpression<FindBoSwimmingClassTypeDataMapper.SwimmingClassTypeWithSubType> {

    private static final long serialVersionUID = 752354049L;

    public QFindBoSwimmingClassTypeDataMapper_SwimmingClassTypeWithSubType(com.querydsl.core.types.Expression<Long> swimmingClassTypeId, com.querydsl.core.types.Expression<com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName> swimmingClassTypeName, com.querydsl.core.types.Expression<Long> swimmingClassSubTypeId, com.querydsl.core.types.Expression<String> swimmingClassSubTypeName) {
        super(FindBoSwimmingClassTypeDataMapper.SwimmingClassTypeWithSubType.class, new Class<?>[]{long.class, com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.class, long.class, String.class}, swimmingClassTypeId, swimmingClassTypeName, swimmingClassSubTypeId, swimmingClassSubTypeName);
    }

}

