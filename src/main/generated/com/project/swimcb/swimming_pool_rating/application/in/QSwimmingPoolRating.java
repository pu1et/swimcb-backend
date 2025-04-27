package com.project.swimcb.swimming_pool_rating.application.in;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingPoolRating is a Querydsl query type for SwimmingPoolRating
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingPoolRating extends EntityPathBase<SwimmingPoolRating> {

    private static final long serialVersionUID = 1930791414L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingPoolRating swimmingPoolRating = new QSwimmingPoolRating("swimmingPoolRating");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.swimcb.member.domain.QMember member;

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool swimmingPool;

    public QSwimmingPoolRating(String variable) {
        this(SwimmingPoolRating.class, forVariable(variable), INITS);
    }

    public QSwimmingPoolRating(Path<? extends SwimmingPoolRating> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingPoolRating(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingPoolRating(PathMetadata metadata, PathInits inits) {
        this(SwimmingPoolRating.class, metadata, inits);
    }

    public QSwimmingPoolRating(Class<? extends SwimmingPoolRating> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.swimcb.member.domain.QMember(forProperty("member")) : null;
        this.swimmingPool = inits.isInitialized("swimmingPool") ? new com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool(forProperty("swimmingPool"), inits.get("swimmingPool")) : null;
    }

}

