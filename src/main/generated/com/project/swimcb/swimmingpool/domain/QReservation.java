package com.project.swimcb.swimmingpool.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = -1001057134L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final com.project.swimcb.common.entity.QBaseEntity _super = new com.project.swimcb.common.entity.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> canceledAt = createDateTime("canceledAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.swimcb.member.domain.QMember member;

    public final NumberPath<Integer> paymentAmount = createNumber("paymentAmount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> paymentApprovedAt = createDateTime("paymentApprovedAt", java.time.LocalDateTime.class);

    public final EnumPath<com.project.swimcb.swimmingpool.domain.enums.PaymentMethod> paymentMethod = createEnum("paymentMethod", com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.class);

    public final DateTimePath<java.time.LocalDateTime> paymentPendingAt = createDateTime("paymentPendingAt", java.time.LocalDateTime.class);

    public final SimplePath<com.project.swimcb.bo.swimmingpool.domain.AccountNo> refundAccountNo = createSimple("refundAccountNo", com.project.swimcb.bo.swimmingpool.domain.AccountNo.class);

    public final NumberPath<Integer> refundAmount = createNumber("refundAmount", Integer.class);

    public final StringPath refundBankName = createString("refundBankName");

    public final DateTimePath<java.time.LocalDateTime> refundedAt = createDateTime("refundedAt", java.time.LocalDateTime.class);

    public final EnumPath<com.project.swimcb.swimmingpool.domain.enums.ReservationStatus> reservationStatus = createEnum("reservationStatus", com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.class);

    public final DateTimePath<java.time.LocalDateTime> reservedAt = createDateTime("reservedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> ticketId = createNumber("ticketId", Long.class);

    public final EnumPath<com.project.swimcb.swimmingpool.domain.enums.TicketType> ticketType = createEnum("ticketType", com.project.swimcb.swimmingpool.domain.enums.TicketType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public final NumberPath<Integer> waitingNo = createNumber("waitingNo", Integer.class);

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservation(PathMetadata metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.swimcb.member.domain.QMember(forProperty("member")) : null;
    }

}

