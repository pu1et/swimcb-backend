package com.project.swimcb.bo.swimmingclass.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingClassSubType is a Querydsl query type for SwimmingClassSubType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingClassSubType extends EntityPathBase<SwimmingClassSubType> {

    private static final long serialVersionUID = 1328039130L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingClassSubType swimmingClassSubType = new QSwimmingClassSubType("swimmingClassSubType");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QSwimmingClassType swimmingClassType;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QSwimmingClassSubType(String variable) {
        this(SwimmingClassSubType.class, forVariable(variable), INITS);
    }

    public QSwimmingClassSubType(Path<? extends SwimmingClassSubType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingClassSubType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingClassSubType(PathMetadata metadata, PathInits inits) {
        this(SwimmingClassSubType.class, metadata, inits);
    }

    public QSwimmingClassSubType(Class<? extends SwimmingClassSubType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.swimmingClassType = inits.isInitialized("swimmingClassType") ? new QSwimmingClassType(forProperty("swimmingClassType"), inits.get("swimmingClassType")) : null;
    }

}

