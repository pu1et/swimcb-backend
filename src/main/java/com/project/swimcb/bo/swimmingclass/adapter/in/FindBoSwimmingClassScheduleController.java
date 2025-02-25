package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassScheduleResponse.ScheduleByDay;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassScheduleResponse.ScheduleByProgram;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassScheduleResponse.ScheduleByTime;
import com.project.swimcb.bo.swimmingclass.domain.enums.FindBoSwimmingClassScheduleByProgramsSwimmingClassType;
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
@RequestMapping("/api/bo/swimming-classes/schedule")
@RequiredArgsConstructor
public class FindBoSwimmingClassScheduleController {

  @Operation(summary = "클래스 일정 보기")
  @GetMapping
  public FindBoSwimmingClassScheduleResponse findBoSwimmingClassSchedule(
      @Parameter(description = "월", example = "2") @Min(1) @Max(12) @RequestParam(value = "month") int month,
      @Parameter(description = "클래스 타입", example = "GROUP") @RequestParam(value = "type", required = false) FindBoSwimmingClassScheduleByProgramsSwimmingClassType type
  ) {
    return new FindBoSwimmingClassScheduleResponse(
        List.of(
            scheduleByDay("월"),
            scheduleByDay("화"),
            scheduleByDay("수"),
            scheduleByDay("목"),
            emptyDay("금"),
            emptyDay("토"),
            emptyDay("일")
        ));
  }

  private ScheduleByDay scheduleByDay(String day) {
    return ScheduleByDay.builder()
        .day(day)
        .programs(List.of(
            scheduleByTime(6),
            scheduleByTime(7),
            emptySchedule(8),
            emptySchedule(9),
            emptySchedule(10),
            emptySchedule(11),
            emptySchedule(12),
            emptySchedule(13),
            emptySchedule(14),
            emptySchedule(15),
            emptySchedule(16),
            emptySchedule(17)
        )).build();
  }

  private ScheduleByDay emptyDay(String day) {
    return ScheduleByDay.builder()
        .day(day)
        .programs(List.of())
        .build();
  }

  private ScheduleByTime scheduleByTime(int hour) {
    return ScheduleByTime.builder()
        .time(LocalTime.of(hour, 0))
        .programs(List.of(
            ScheduleByProgram.builder()
                .swimmingClassId(1L)
                .SwimmingClassType("단체강습")
                .SwimmingClassSubType("초급")
                .instructorNames(List.of("김준호"))
                .build(),
            ScheduleByProgram.builder()
                .swimmingClassId(2L)
                .SwimmingClassType("단체강습")
                .SwimmingClassSubType("중급")
                .instructorNames(List.of("최성민"))
                .build(),
            ScheduleByProgram.builder()
                .swimmingClassId(3L)
                .SwimmingClassType("단체강습")
                .SwimmingClassSubType("상급")
                .instructorNames(List.of("우지현"))
                .build(),
            ScheduleByProgram.builder()
                .swimmingClassId(4L)
                .SwimmingClassType("단체강습")
                .SwimmingClassSubType("중급")
                .instructorNames(List.of("연수"))
                .build(),
            ScheduleByProgram.builder()
                .swimmingClassId(5L)
                .SwimmingClassType("레슨")
                .SwimmingClassSubType("개인강습")
                .instructorNames(List.of("원길동"))
                .build(),
            ScheduleByProgram.builder()
                .swimmingClassId(6L)
                .SwimmingClassType("아동수영")
                .SwimmingClassSubType("병아리반")
                .instructorNames(List.of("홍지원"))
                .build(),
            ScheduleByProgram.builder()
                .swimmingClassId(7L)
                .SwimmingClassType("안전근무")
                .SwimmingClassSubType("안전근무")
                .instructorNames(List.of("정상훈", "고지훈"))
                .build()))
        .build();
  }

  private ScheduleByTime emptySchedule(int hour) {
    return ScheduleByTime.builder()
        .time(LocalTime.of(hour, 0))
        .programs(List.of())
        .build();
  }
}

