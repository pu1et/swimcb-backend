package com.project.swimcb.bo.freeswimming.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateBoFreeSwimmingRequest(

    @Min(value = 1, message = "강습 월은 1 이상이어야 합니다.")
    @Max(value = 12, message = "강습 월은 12 이하여야 합니다.")
    int month,

    @NotNull(message = "자유수영 요일은 null이 될 수 없습니다.")
    @Schema(description = "자유수영 요일")
    List<DayOfWeek> days,

    @Valid
    @NotNull(message = "자유수영 시작/종료 시간은 null이 될 수 없습니다.")
    @Schema(description = "자유수영 시작/종료 시간")
    Time time,

    @Schema(description = "담당강사 ID", example = "1")
    Long instructorId,

    @Valid
    @NotNull(message = "자유수영 티켓 리스트는 null이 될 수 없습니다.")
    @Schema(description = "자유수영 티켓 리스트")
    List<Ticket> tickets,

    @Valid
    @NotNull(message = "정원 정보는 null이 될 수 없습니다.")
    @Schema(description = "정원 정보")
    RegistrationCapacity registrationCapacity,

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

  @Builder
  @Schema(name = "CreateBoFreeSwimmingRequest.RegistrationCapacity")
  record RegistrationCapacity(

      @NotNull(message = "총 정원은 null이 될 수 없습니다.")
      @Schema(description = "총 정원", example = "20")
      Integer totalCapacity,

      @NotNull(message = "예약 허용 인원은 null이 될 수 없습니다.")
      @Schema(description = "예약 허용 인원", example = "11")
      Integer reservationLimitCount
  ) {

  }

}
