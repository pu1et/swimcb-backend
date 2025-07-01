package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
import static com.querydsl.core.types.Projections.constructor;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingClassTicketGateway;
import com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo.SwimmingClass;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindSwimmingClassTicketDataMapper implements FindSwimmingClassTicketGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public SwimmingClassTicketInfo findSwimmingClassTicket(long ticketId) {
    val ticket = queryFactory.select(constructor(QuerySwimmingClassTicketInfo.class,
            swimmingClassEntity.id,
            swimmingClassEntity.month,
            swimmingClassEntity.reservationLimitCount,
            swimmingClassEntity.reservedCount,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,
            ticketEntity.name,
            ticketEntity.price
        ))
        .from(swimmingClassEntity)
        .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
        .join(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
        .join(ticketEntity)
        .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
        .where(
            ticketEntity.targetType.eq(SWIMMING_CLASS),
            ticketEntity.id.eq(ticketId)
        )
        .fetchFirst();

    if (ticket == null) {
      throw new NoSuchElementException("수영 클래스 티켓이 존재하지 않습니다 : " + ticketId);
    }

    return SwimmingClassTicketInfo.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .id(ticket.swimmingClassId())
                .month(ticket.month())
                .type(ticket.clssType().getDescription())
                .subType(ticket.classSubType())
                .days(bitVectorToDays(ticket.daysOfWeek()))
                .startTime(ticket.startTime())
                .endTime(ticket.endTime())
                .availabilityStatus(
                    SwimmingClassAvailabilityStatus.calculateStatus(
                        ticket.reservationLimitCount,
                        ticket.reservedCount))
                .build()
        )
        .ticket(
            SwimmingClassTicketInfo.SwimmingClassTicket.builder()
                .name(ticket.ticketName())
                .price(ticket.ticketPrice())
                .build()
        )
        .build();
  }

  List<String> bitVectorToDays(int bitVector) {
    val dayMap = Map.of(
        MONDAY, "월",
        TUESDAY, "화",
        WEDNESDAY, "수",
        THURSDAY, "목",
        FRIDAY, "금",
        SATURDAY, "토",
        SUNDAY, "일"
    );

    return IntStream.range(0, 7)
        .filter(i -> (bitVector & (1 << (6 - i))) > 0)
        .mapToObj(i -> dayMap.get(DayOfWeek.of(i + 1)))
        .toList();
  }

  @Builder
  public record QuerySwimmingClassTicketInfo(
      long swimmingClassId,
      int month,
      int reservationLimitCount,
      int reservedCount,
      SwimmingClassTypeName clssType,
      String classSubType,
      int daysOfWeek,
      LocalTime startTime,
      LocalTime endTime,
      String ticketName,
      int ticketPrice
  ) {

    @QueryProjection
    public QuerySwimmingClassTicketInfo {
    }

  }

}
