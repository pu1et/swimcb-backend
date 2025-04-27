package com.project.swimcb.bo.swimmingpool.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingPoolImage is a Querydsl query type for SwimmingPoolImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingPoolImage extends EntityPathBase<SwimmingPoolImage> {

    private static final long serialVersionUID = 862598993L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingPoolImage swimmingPoolImage = new QSwimmingPoolImage("swimmingPoolImage");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath path = createString("path");

    public final QSwimmingPool swimmingPool;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QSwimmingPoolImage(String variable) {
        this(SwimmingPoolImage.class, forVariable(variable), INITS);
    }

    public QSwimmingPoolImage(Path<? extends SwimmingPoolImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingPoolImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingPoolImage(PathMetadata metadata, PathInits inits) {
        this(SwimmingPoolImage.class, metadata, inits);
    }

    public QSwimmingPoolImage(Class<? extends SwimmingPoolImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.swimmingPool = inits.isInitialized("swimmingPool") ? new QSwimmingPool(forProperty("swimmingPool"), inits.get("swimmingPool")) : null;
    }

}

