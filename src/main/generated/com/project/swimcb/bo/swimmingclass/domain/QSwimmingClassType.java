package com.project.swimcb.bo.swimmingclass.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingClassType is a Querydsl query type for SwimmingClassType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingClassType extends EntityPathBase<SwimmingClassType> {

    private static final long serialVersionUID = 1531163642L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingClassType swimmingClassType = new QSwimmingClassType("swimmingClassType");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName> name = createEnum("name", com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.class);

    public final com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool swimmingPool;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QSwimmingClassType(String variable) {
        this(SwimmingClassType.class, forVariable(variable), INITS);
    }

    public QSwimmingClassType(Path<? extends SwimmingClassType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingClassType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingClassType(PathMetadata metadata, PathInits inits) {
        this(SwimmingClassType.class, metadata, inits);
    }

    public QSwimmingClassType(Class<? extends SwimmingClassType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.swimmingPool = inits.isInitialized("swimmingPool") ? new com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool(forProperty("swimmingPool"), inits.get("swimmingPool")) : null;
    }

}

