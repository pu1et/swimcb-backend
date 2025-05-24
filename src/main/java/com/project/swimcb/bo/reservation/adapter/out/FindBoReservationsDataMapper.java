package com.project.swimcb.bo.reservation.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.project.swimcb.member.domain.QMember.member;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.REFUND_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_CANCELLED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.querydsl.core.types.Projections.constructor;
import static java.time.LocalTime.MAX;

import com.project.swimcb.bo.reservation.application.port.out.FindBoReservationsDsGateway;
import com.project.swimcb.bo.reservation.domain.BoReservation;
import com.project.swimcb.bo.reservation.domain.BoReservation.Cancel;
import com.project.swimcb.bo.reservation.domain.BoReservation.Member;
import com.project.swimcb.bo.reservation.domain.BoReservation.Payment;
import com.project.swimcb.bo.reservation.domain.BoReservation.Refund;
import com.project.swimcb.bo.reservation.domain.BoReservation.ReservationDetail;
import com.project.swimcb.bo.reservation.domain.BoReservation.SwimmingClass;
import com.project.swimcb.bo.reservation.domain.FindBoReservationsCondition;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.NonNull;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
class FindBoReservationsDataMapper implements FindBoReservationsDsGateway {

  private final JPAQueryFactory queryFactory;

  public FindBoReservationsDataMapper(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public Page<BoReservation> findBoReservations(@NonNull FindBoReservationsCondition condition) {
    val result = queryFactory.select(constructor(QueryReservation.class,
            member.id,
            member.name,
            member.birthDate,

            swimmingClass.id,
            swimmingClassType.name,
            swimmingClassSubType.name,
            swimmingClass.daysOfWeek,
            swimmingClass.startTime,
            swimmingClass.endTime,

            reservation.id,
            reservation.ticketType,
            reservation.reservationStatus,
            reservation.waitingNo,
            reservation.reservedAt,
            reservation.paymentPendingAt,
            reservation.paymentVerificationAt,
            reservation.paymentApprovedAt,
            reservation.canceledAt,
            reservation.refundedAt,

            reservation.paymentMethod,
            reservation.paymentAmount,

            reservation.cancellationReason,

            reservation.refundAmount,
            reservation.refundAccountNo,
            reservation.refundBankName,
            reservation.refundAccountHolder
        ))
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
        .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
        .join(reservation.member, member)
        .where(
            swimmingClass.swimmingPool.id.eq(condition.swimmingPoolId()),
            reservation.reservedAt.between(
                condition.startDate().atStartOfDay(), condition.endDate().atTime(MAX)),
            programTypeEqIfExists(condition.programType()),
            swimmingClassIdEqIfExists(condition.swimmingClassId()),
            reservationStatusEqIfExists(condition.reservationStatus()),
            paymentMethodEqIfExists(condition.paymentMethod())
        )
        .orderBy(reservation.reservedAt.desc())
        .offset(condition.pageable().getOffset())
        .limit(condition.pageable().getPageSize())
        .fetch()
        .stream()
        .map(this::reservation)
        .toList();

    val updatedResult = updateWithCurrentWaitingNo(result, condition.swimmingPoolId());

    val count = queryFactory.select(reservation.id.count())
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
        .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
        .join(reservation.member, member)
        .where(
            swimmingClass.swimmingPool.id.eq(condition.swimmingPoolId()),
            reservation.reservedAt.between(
                condition.startDate().atStartOfDay(), condition.endDate().atTime(MAX)),
            programTypeEqIfExists(condition.programType()),
            swimmingClassIdEqIfExists(condition.swimmingClassId()),
            reservationStatusEqIfExists(condition.reservationStatus()),
            paymentMethodEqIfExists(condition.paymentMethod())
        )
        .fetchOne();

    return new PageImpl<>(updatedResult, condition.pageable(), count);
  }

  List<BoReservation> updateWithCurrentWaitingNo(
      @NonNull List<BoReservation> reservations,
      @NonNull Long swimmingPoolId
  ) {
    // 1. RESERVATION_PENDING 상태인 예약만 필터링 & 클래스 ID 추출
    val classIds = reservations.stream()
        .filter(r -> r.reservationDetail().status() == RESERVATION_PENDING)
        .map(r -> r.swimmingClass().id())
        .collect(Collectors.toSet());

    if (classIds.isEmpty()) {
      return reservations;
    }

    // 2. 전체 대기 예약 조회 후 클래스별 정렬된 리스트 구성
    val groupedSorted = findAllWaitingReservationsByClasses(
        swimmingPoolId, classIds).stream()
        .collect(Collectors.groupingBy(
            WaitingReservation::classId,
            Collectors.collectingAndThen(
                Collectors.toList(),
                list -> list.stream()
                    .sorted(Comparator.comparing(WaitingReservation::waitingNo))
                    .toList()
            )
        ));

    // 3. reservationId -> waitingNo 매핑 (1부터 시작)
    val waitingNoMap = groupedSorted.entrySet().stream()
        .flatMap(entry -> IntStream.range(0, entry.getValue().size())
            .mapToObj(i -> Map.entry(entry.getValue().get(i).reservationId(), i + 1)))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    // 4. BoReservation 업데이트
    return reservations.stream()
        .map(r -> {
          if (r.reservationDetail().status() != RESERVATION_PENDING) {
            return r;
          }
          val reservationId = r.reservationDetail().id();
          val newNo = waitingNoMap.get(reservationId);
          return (newNo != null)
              ? r.withReservationDetail(r.reservationDetail().withWaitingNo(newNo))
              : r;
        })
        .toList();
  }

  List<WaitingReservation> findAllWaitingReservationsByClasses(
      @NonNull Long swimmingPoolId,
      @NonNull Set<Long> classIds
  ) {
    return queryFactory.select(constructor(WaitingReservation.class,
            reservation.id,
            reservation.waitingNo,
            swimmingClass.id
        ))
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .where(
            swimmingClass.swimmingPool.id.eq(swimmingPoolId),
            swimmingClass.id.in(classIds),
            reservation.reservationStatus.eq(RESERVATION_PENDING)
        )
        .fetch();
  }

  private BooleanExpression programTypeEqIfExists(TicketType programType) {
    if (programType == null) {
      return null;
    }
    return reservation.ticketType.eq(programType);
  }

  private BooleanExpression swimmingClassIdEqIfExists(Long swimmingClassId) {
    if (swimmingClassId == null) {
      return null;
    }
    return swimmingClass.id.eq(swimmingClassId);
  }

  private BooleanExpression reservationStatusEqIfExists(ReservationStatus reservationStatus) {
    if (reservationStatus == null) {
      return null;
    }
    return reservation.reservationStatus.eq(reservationStatus);
  }

  private BooleanExpression paymentMethodEqIfExists(PaymentMethod paymentMethod) {
    if (paymentMethod == null) {
      return null;
    }
    return reservation.paymentMethod.eq(paymentMethod);
  }

  private BoReservation reservation(@NonNull QueryReservation i) {
    return BoReservation.builder()
        .member(
            Member.builder()
                .id(i.memberId())
                .name(i.name())
                .birthDate(i.birthDate())
                .build()
        )
        .swimmingClass(
            SwimmingClass.builder()
                .id(i.swimmingClassId())
                .type(i.swimmingClassType())
                .subType(i.swimmingClassSubType())
                .daysOfWeek(ClassDayOfWeek.of(i.daysOfWeek()))
                .startTime(i.startTime())
                .endTime(i.endTime())
                .build()
        )
        .reservationDetail(
            ReservationDetail.builder()
                .id(i.reservationId())
                .ticketType(i.ticketType())
                .status(i.reservationStatus())
                .reservedAt(i.reservedAt())
                .waitingNo(i.waitingNo())
                .build()
        )
        .payment(
            Payment.builder()
                .method(i.paymentMethod())
                .amount(i.amount())
                .pendingAt(i.paymentPendingAt())
                .verificationAt(i.paymentVerificationAt())
                .completedAt(i.paymentCompletedAt())
                .build()
        )
        .cancel(cancel(i))
        .refund(refund(i))
        .build();
  }

  private Cancel cancel(@NonNull FindBoReservationsDataMapper.QueryReservation i) {
    if (i.reservationStatus != RESERVATION_CANCELLED) {
      return null;
    }
    return Cancel.builder()
        .reason(i.cancellationReason())
        .canceledAt(i.canceledAt())
        .build();
  }

  private Refund refund(@NonNull FindBoReservationsDataMapper.QueryReservation i) {
    if (i.reservationStatus != REFUND_COMPLETED) {
      return null;
    }
    return Refund.builder()
        .amount(i.refundAmount())
        .accountNo(i.refundAccountNo())
        .bankName(i.refundBankName())
        .accountHolder(i.refundAccountHolder())
        .refundedAt(i.refundedAt())
        .build();
  }

  @Builder
  protected record QueryReservation(
      @NonNull Long memberId,
      @NonNull String name,
      @NonNull LocalDate birthDate,

      @NonNull Long swimmingClassId,
      @NonNull SwimmingClassTypeName swimmingClassType,
      @NonNull String swimmingClassSubType,
      @NonNull Integer daysOfWeek,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,

      @NonNull Long reservationId,
      @NonNull TicketType ticketType,
      @NonNull ReservationStatus reservationStatus,
      Integer waitingNo,
      @NonNull LocalDateTime reservedAt,
      LocalDateTime paymentPendingAt,
      LocalDateTime paymentVerificationAt,
      LocalDateTime paymentCompletedAt,
      LocalDateTime canceledAt,
      LocalDateTime refundedAt,

      @NonNull PaymentMethod paymentMethod,
      @NonNull Integer amount,

      CancellationReason cancellationReason,

      Integer refundAmount,
      AccountNo refundAccountNo,
      String refundBankName,
      String refundAccountHolder
  ) {

    @QueryProjection
    public QueryReservation {
    }

  }

  @Builder
  protected record WaitingReservation(
      long reservationId,
      int waitingNo,
      long classId
  ) {

    @QueryProjection
    public WaitingReservation {
    }

  }

}
