package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.Days;
import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.FreeSwimming;
import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.RegistrationCapacity;
import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.Ticket;
import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.TicketPriceRange;
import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.Time;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/free-swimming")
@RequiredArgsConstructor
public class FindBoFreeSwimmingController {

  @Operation(summary = "자유수영 데이터 관리 - 자유수영 리스트 조회")
  @GetMapping
  public FindBoFreeSwimmingResponse findBoFreeSwimming() {
    return FindBoFreeSwimmingResponse.builder()
        .freeSwimmings(
            List.of(
                freeSwimming(1L),
                freeSwimming(2L),
                freeSwimming(3L)
            )
        )
        .build();
  }

  private FreeSwimming freeSwimming(long freeSwimmingId) {
    return FreeSwimming.builder()
        .freeSwimmingId(freeSwimmingId)
        .days(
            Days.builder()
                .monday(true)
                .tuesday(false)
                .wednesday(true)
                .tuesday(false)
                .friday(false)
                .build()
        )
        .time(
            Time.builder()
                .startTime(LocalTime.of(6, 0))
                .endTime(LocalTime.of(6, 50))
                .build()
        )
        .instructorName("신우진")
        .ticketPriceRange(
            TicketPriceRange.builder()
                .minimumPrice(10000)
                .maximumPrice(50000)
                .build()
        )
        .tickets(List.of(
            Ticket.builder()
                .name("성인일반")
                .price(10000)
                .build(),
            Ticket.builder()
                .name("청소년")
                .price(8000)
                .build()
        ))
        .registrationCapacity(RegistrationCapacity.builder()
            .totalCapacity(20)
            .reservationLimitCount(19)
            .completedReservationCount(17)
            .remainingReservationCount(2)
            .build())
        .build();
  }
}

