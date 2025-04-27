package com.project.swimcb.bo.swimmingpool.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSwimmingPool is a Querydsl query type for SwimmingPool
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSwimmingPool extends EntityPathBase<SwimmingPool> {

    private static final long serialVersionUID = -1796174806L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSwimmingPool swimmingPool = new QSwimmingPool("swimmingPool");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    public final SimplePath<AccountNo> accountNo = createSimple("accountNo", AccountNo.class);

    public final StringPath address = createString("address");

    public final com.project.swimcb.bo.admin.domain.QAdmin admin;

    public final StringPath closedDays = createString("closedDays");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isNewRegistrationExtended = createBoolean("isNewRegistrationExtended");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> newRegistrationPeriodEndDay = createNumber("newRegistrationPeriodEndDay", Integer.class);

    public final NumberPath<Integer> newRegistrationPeriodStartDay = createNumber("newRegistrationPeriodStartDay", Integer.class);

    public final StringPath operatingDays = createString("operatingDays");

    public final StringPath phone = createString("phone");

    public final NumberPath<Integer> reRegistrationPeriodEndDay = createNumber("reRegistrationPeriodEndDay", Integer.class);

    public final NumberPath<Integer> reRegistrationPeriodStartDay = createNumber("reRegistrationPeriodStartDay", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public final StringPath usageAgreementPath = createString("usageAgreementPath");

    public QSwimmingPool(String variable) {
        this(SwimmingPool.class, forVariable(variable), INITS);
    }

    public QSwimmingPool(Path<? extends SwimmingPool> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSwimmingPool(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSwimmingPool(PathMetadata metadata, PathInits inits) {
        this(SwimmingPool.class, metadata, inits);
    }

    public QSwimmingPool(Class<? extends SwimmingPool> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new com.project.swimcb.bo.admin.domain.QAdmin(forProperty("admin")) : null;
    }

}

