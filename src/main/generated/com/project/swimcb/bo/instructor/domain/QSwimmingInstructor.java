package com.project.swimcb.bo.instructor.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingInstructor is a Querydsl query type for SwimmingInstructor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingInstructor extends EntityPathBase<SwimmingInstructor> {

    private static final long serialVersionUID = 271026255L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingInstructor swimmingInstructor = new QSwimmingInstructor("swimmingInstructor");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool swimmingPool;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QSwimmingInstructor(String variable) {
        this(SwimmingInstructor.class, forVariable(variable), INITS);
    }

    public QSwimmingInstructor(Path<? extends SwimmingInstructor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingInstructor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingInstructor(PathMetadata metadata, PathInits inits) {
        this(SwimmingInstructor.class, metadata, inits);
    }

    public QSwimmingInstructor(Class<? extends SwimmingInstructor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.swimmingPool = inits.isInitialized("swimmingPool") ? new com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool(forProperty("swimmingPool"), inits.get("swimmingPool")) : null;
    }

}

