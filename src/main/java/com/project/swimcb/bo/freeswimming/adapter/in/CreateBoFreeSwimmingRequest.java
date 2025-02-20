package com.project.swimcb.bo.freeswimming.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateBoFreeSwimmingRequest(

    @NotNull(message = "자유수영 요일은 null이 될 수 없습니다.")
    @Schema(description = "자유수영 요일")
    Days days,

    @Valid
    @NotNull(message = "자유수영 시작/종료 시간은 null이 될 수 없습니다.")
    @Schema(description = "자유수영 시작/종료 시간")
    Time time,

    @NotNull(message = "담당강사는 null이 될 수 없습니다.")
    @Schema(description = "담당강사", example = "손지혜")
    String instructorName,

    @Valid
    @NotNull(message = "자유수영 티켓 리스트는 null이 될 수 없습니다.")
    @Schema(description = "자유수영 티켓 리스트")
    List<Ticket> tickets,

    @NotNull(message = "정원 정보는 null이 될 수 없습니다.")
    @Schema(description = "정원 정보")
    RegistrationCapacity registrationCapacity,

    @Schema(description = "사용자 노출 여부", example = "true")
    boolean isExposed
) {

  @Builder
  record Days(
      @Schema(description = "월요일 수업 여부", example = "true")
      boolean monday,
      @Schema(description = "화요일 수업 여부", example = "false")
      boolean tuesday,
      @Schema(description = "수요일 수업 여부", example = "true")
      boolean wednesday,
      @Schema(description = "목요일 수업 여부", example = "false")
      boolean thursday,
      @Schema(description = "금요일 수업 여부", example = "false")
      boolean friday,
      @Schema(description = "토요일 수업 여부", example = "false")
      boolean saturday,
      @Schema(description = "일요일 수업 여부", example = "false")
      boolean sunday
  ) {

  }

  @Builder
  record Time(
      @NotNull(message = "자유수영 시작 시간은 null이 될 수 없습니다.")
      @Schema(description = "자유수영 시작 시간", example = "06:00")
      LocalTime startTime,

      @NotNull(message = "자유수영 종료 시간은 null이 될 수 없습니다.")
      @Schema(description = "자유수영ㄱ 종료 시간", example = "06:50")
      LocalTime endTime
  ) {

  }

  @Builder
  record Ticket(
      @NotNull(message = "티켓 이름은 null이 될 수 없습니다.")
      @Schema(description = "티켓 이름", example = "성인일반")
      String name,
      @Schema(description = "티켓 가격", example = "100000")
      int price
  ) {

  }

  @Builder
  record RegistrationCapacity(
      @Schema(description = "총 정원", example = "20")
      int totalCapacity,
      @Schema(description = "예약 허용 인원", example = "11")
      int reservationLimitCount
  ) {

  }
}
