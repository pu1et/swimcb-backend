package com.project.swimcb.swimming_pool_review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingPoolReview is a Querydsl query type for SwimmingPoolReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingPoolReview extends EntityPathBase<SwimmingPoolReview> {

    private static final long serialVersionUID = 1236345547L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingPoolReview swimmingPoolReview = new QSwimmingPoolReview("swimmingPoolReview");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.swimcb.member.domain.QMember member;

    public final StringPath review = createString("review");

    public final com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool swimmingPool;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QSwimmingPoolReview(String variable) {
        this(SwimmingPoolReview.class, forVariable(variable), INITS);
    }

    public QSwimmingPoolReview(Path<? extends SwimmingPoolReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingPoolReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingPoolReview(PathMetadata metadata, PathInits inits) {
        this(SwimmingPoolReview.class, metadata, inits);
    }

    public QSwimmingPoolReview(Class<? extends SwimmingPoolReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.swimcb.member.domain.QMember(forProperty("member")) : null;
        this.swimmingPool = inits.isInitialized("swimmingPool") ? new com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool(forProperty("swimmingPool"), inits.get("swimmingPool")) : null;
    }

}

