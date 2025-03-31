package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPoolImage.swimmingPoolImage;
import static com.querydsl.core.types.Projections.constructor;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingClassTicketGateway;
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
            swimmingClass.id,
            swimmingClassType.name,
            swimmingClassSubType.name,
            swimmingClass.daysOfWeek,
            swimmingClass.startTime,
            swimmingClass.endTime,
            swimmingClassTicket.name,
            swimmingClassTicket.price
        ))
        .from(swimmingClass)
        .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
        .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
        .join(swimmingClassTicket).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .where(
            swimmingClassTicket.id.eq(ticketId)
        )
        .fetchFirst();

    if (ticket == null) {
      throw new NoSuchElementException("수영 클래스 티켓이 존재하지 않습니다 : " + ticketId);
    }

    return SwimmingClassTicketInfo.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .id(ticket.swimmingClassId())
                .type(ticket.clssType().getDescription())
                .subType(ticket.classSubType())
                .days(bitVectorToDays(ticket.daysOfWeek()))
                .startTime(ticket.startTime())
                .endTime(ticket.endTime())
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
