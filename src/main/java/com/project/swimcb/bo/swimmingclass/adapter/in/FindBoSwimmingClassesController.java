package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Days;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Ticket;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.TicketPriceRange;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Type;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes")
@RequiredArgsConstructor
public class FindBoSwimmingClassesController {

  @Operation(summary = "클래스 데이터 관리 - 클래스 조회")
  @GetMapping
  public FindBoSwimmingClassesResponse findBoSwimmingClasses(
      @Parameter(description = "월", example = "2") @Min(1) @Max(12) @RequestParam(value = "month") int month
  ) {
    return FindBoSwimmingClassesResponse.builder()
        .swimmingClasses(
            List.of(
                swimmingClass(1L),
                swimmingClass(2L),
                swimmingClass(3L)
            )
        )
        .build();
  }

  private SwimmingClass swimmingClass(long swimmingClassId) {
    return SwimmingClass.builder()
        .swimmingClassId(swimmingClassId)
        .type(
            Type.builder()
                .type("단체강습")
                .subType("마스터즈")
                .build()
        )
        .days(
            Days.builder()
                .isMonday(true)
                .isTuesday(false)
                .isWednesday(true)
                .isThursday(false)
                .isFriday(false)
                .isSaturday(false)
                .isSunday(false)
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

