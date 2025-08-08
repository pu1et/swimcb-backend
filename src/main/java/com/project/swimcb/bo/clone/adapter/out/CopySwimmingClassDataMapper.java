package com.project.swimcb.bo.clone.adapter.out;

import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingInstructorEntity.swimmingInstructorEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.bo.clone.application.port.out.CopySwimmingClassDsGateway;
import com.project.swimcb.bo.clone.domain.SwimmingClassCopyCandidate;
import com.project.swimcb.bo.clone.domain.SwimmingClassCopyCandidate.Ticket;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CopySwimmingClassDataMapper implements CopySwimmingClassDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<SwimmingClassCopyCandidate> findAllSwimmingClassesByMonth(@NonNull YearMonth month) {
    return queryFactory.select(constructor(QuerySwimmingClassCopyCandidate.class,
            swimmingPoolEntity.id,
            swimmingClassEntity.id,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,
            swimmingClassTypeEntity.id,
            swimmingClassSubTypeEntity.id,
            swimmingInstructorEntity.id,
            ticketEntity.name,
            ticketEntity.price,
            swimmingClassEntity.totalCapacity,
            swimmingClassEntity.reservationLimitCount
        ))
        .from(swimmingPoolEntity)
        .join(swimmingClassEntity).on(swimmingClassEntity.swimmingPool.eq(swimmingPoolEntity))
        .join(swimmingClassEntity.type, swimmingClassTypeEntity)
        .join(swimmingClassEntity.subType, swimmingClassSubTypeEntity)
        .leftJoin(swimmingClassEntity.instructor, swimmingInstructorEntity)
        .join(ticketEntity).on(
            ticketEntity.targetType.eq(SWIMMING_CLASS),
            ticketEntity.targetId.eq(swimmingClassEntity.id)
        )
        .where(
            swimmingPoolEntity.name.isNotNull(),
            swimmingPoolEntity.address.isNotNull(),
            swimmingPoolEntity.latitude.isNotNull(),
            swimmingPoolEntity.longitude.isNotNull(),

            swimmingClassEntity.year.eq(month.getYear()),
            swimmingClassEntity.month.eq(month.getMonthValue()),
            swimmingClassEntity.isVisible.isTrue(),
            swimmingClassEntity.isCanceled.isFalse(),

            ticketEntity.isDeleted.isFalse()
        )
        .fetch()
        .stream()
        .collect(Collectors.groupingBy(i -> i.swimmingClassId))
        .values()
        .stream()
        .map(candidates -> {
          val firstCandidate = candidates.getFirst();

          return SwimmingClassCopyCandidate.builder()
              .swimmingPoolId(firstCandidate.swimmingPoolId())
              .dayOfWeek(ClassDayOfWeek.of(firstCandidate.days()))
              .time(
                  SwimmingClassCopyCandidate.Time.builder()
                      .startTime(firstCandidate.startTime())
                      .endTime(firstCandidate.endTime())
                      .build()
              )
              .type(
                  SwimmingClassCopyCandidate.Type.builder()
                      .classTypeId(firstCandidate.swimmingClassTypeId())
                      .classSubTypeId(firstCandidate.swimmingClassSubTypeId())
                      .build()
              )
              .instructorId(firstCandidate.instructorId())
              .tickets(
                  candidates.stream()
                      .map(i -> Ticket.builder()
                          .name(i.ticketName())
                          .price(i.ticketPrice())
                          .build())
                      .toList()
              )
              .registrationCapacity(
                  SwimmingClassCopyCandidate.RegistrationCapacity.builder()
                      .totalCapacity(firstCandidate.totalCapacity())
                      .reservationLimitCount(firstCandidate.reservationLimitCount())
                      .build()
              )
              .build();
        })
        .toList();
  }

  protected record QuerySwimmingClassCopyCandidate(
      long swimmingPoolId,
      long swimmingClassId,
      int days,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,
      long swimmingClassTypeId,
      long swimmingClassSubTypeId,
      Long instructorId,
      @NonNull String ticketName,
      int ticketPrice,
      int totalCapacity,
      int reservationLimitCount
  ) {

    @QueryProjection
    public QuerySwimmingClassCopyCandidate {
    }

  }

}
