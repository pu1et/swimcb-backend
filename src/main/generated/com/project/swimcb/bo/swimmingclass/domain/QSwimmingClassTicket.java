package com.project.swimcb.bo.swimmingclass.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingClassTicket is a Querydsl query type for SwimmingClassTicket
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingClassTicket extends EntityPathBase<SwimmingClassTicket> {

    private static final long serialVersionUID = -1740677172L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingClassTicket swimmingClassTicket = new QSwimmingClassTicket("swimmingClassTicket");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QSwimmingClass swimmingClass;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QSwimmingClassTicket(String variable) {
        this(SwimmingClassTicket.class, forVariable(variable), INITS);
    }

    public QSwimmingClassTicket(Path<? extends SwimmingClassTicket> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingClassTicket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingClassTicket(PathMetadata metadata, PathInits inits) {
        this(SwimmingClassTicket.class, metadata, inits);
    }

    public QSwimmingClassTicket(Class<? extends SwimmingClassTicket> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.swimmingClass = inits.isInitialized("swimmingClass") ? new QSwimmingClass(forProperty("swimmingClass"), inits.get("swimmingClass")) : null;
    }

}

