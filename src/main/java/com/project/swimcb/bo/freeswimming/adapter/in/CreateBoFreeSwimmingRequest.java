package com.project.swimcb.bo.freeswimming.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateBoFreeSwimmingRequest(

    @NotNull(message = "년/월은 null이 될 수 없습니다.")
    @Schema(description = "자유수영 년/월", example = "2025-10")
    YearMonth yearMonth,

    @NotNull(message = "자유수영 요일은 null이 될 수 없습니다.")
    @Schema(description = "자유수영 요일")
    List<DayOfWeek> days,

    @Valid
    @NotNull(message = "자유수영 시작/종료 시간은 null이 될 수 없습니다.")
    @Schema(description = "자유수영 시작/종료 시간")
    Time time,

    @Schema(description = "안전근무 ID", example = "1")
    Long lifeguardId,

    @Valid
    @NotNull(message = "자유수영 티켓 리스트는 null이 될 수 없습니다.")
    @Schema(description = "자유수영 티켓 리스트")
    List<Ticket> tickets,

    @NotNull(message = "정원 정보는 null이 될 수 없습니다.")
    @Schema(description = "정원 정보")
    Integer capacity,

    @NotNull(message = "사용자 노출 여부는 null이 될 수 없습니다.")
    @Schema(description = "사용자 노출 여부", example = "true")
    Boolean isExposed
) {

  @Builder
  @Schema(name = "CreateBoFreeSwimmingRequest.Time")
  record Time(

      @NotNull(message = "자유수영 시작 시간은 null이 될 수 없습니다.")
      @Schema(description = "자유수영 시작 시간", example = "06:00")
      LocalTime startTime,

      @NotNull(message = "자유수영 종료 시간은 null이 될 수 없습니다.")
      @Schema(description = "자유수영 종료 시간", example = "06:50")
      LocalTime endTime
  ) {

  }

  @Builder
  @Schema(name = "CreateBoFreeSwimmingRequest.Ticket")
  record Ticket(

      @NotNull(message = "티켓 이름은 null이 될 수 없습니다.")
      @Schema(description = "티켓 이름", example = "성인일반")
      String name,

      @NotNull(message = "티켓 가격은 null이 될 수 없습니다.")
      @Schema(description = "티켓 가격", example = "100000")
      Integer price
  ) {

  }

}
