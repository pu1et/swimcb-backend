package com.project.swimcb.bo.swimmingclass.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingClass is a Querydsl query type for SwimmingClass
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingClass extends EntityPathBase<SwimmingClass> {

    private static final long serialVersionUID = -2134048608L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingClass swimmingClass = new QSwimmingClass("swimmingClass");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Integer> daysOfWeek = createNumber("daysOfWeek", Integer.class);

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.swimcb.bo.instructor.domain.QSwimmingInstructor instructor;

    public final BooleanPath isVisible = createBoolean("isVisible");

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Integer> reservationLimitCount = createNumber("reservationLimitCount", Integer.class);

    public final NumberPath<Integer> reservedCount = createNumber("reservedCount", Integer.class);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final QSwimmingClassSubType subType;

    public final com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool swimmingPool;

    public final NumberPath<Integer> totalCapacity = createNumber("totalCapacity", Integer.class);

    public final QSwimmingClassType type;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QSwimmingClass(String variable) {
        this(SwimmingClass.class, forVariable(variable), INITS);
    }

    public QSwimmingClass(Path<? extends SwimmingClass> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingClass(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingClass(PathMetadata metadata, PathInits inits) {
        this(SwimmingClass.class, metadata, inits);
    }

    public QSwimmingClass(Class<? extends SwimmingClass> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.instructor = inits.isInitialized("instructor") ? new com.project.swimcb.bo.instructor.domain.QSwimmingInstructor(forProperty("instructor"), inits.get("instructor")) : null;
        this.subType = inits.isInitialized("subType") ? new QSwimmingClassSubType(forProperty("subType"), inits.get("subType")) : null;
        this.swimmingPool = inits.isInitialized("swimmingPool") ? new com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool(forProperty("swimmingPool"), inits.get("swimmingPool")) : null;
        this.type = inits.isInitialized("type") ? new QSwimmingClassType(forProperty("type"), inits.get("type")) : null;
    }

}

