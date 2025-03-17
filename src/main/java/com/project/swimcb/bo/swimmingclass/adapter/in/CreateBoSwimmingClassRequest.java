package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand;
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
@Schema(description = "BO 클래스 데이터 관리 - 클래스 추가 response")
public record CreateBoSwimmingClassRequest(

    @Min(value = 1, message = "강습 월은 1 이상이어야 합니다.")
    @Max(value = 12, message = "강습 월은 12 이하여야 합니다.")
    int month,

    @NotNull(message = "강습 요일은 null이 될 수 없습니다.")
    @Schema(description = "강습 요일")
    List<DayOfWeek> days,

    @Valid
    @NotNull(message = "강습 시작/종료 시간은 null이 될 수 없습니다.")
    @Schema(description = "강습 시작/종료 시간")
    Time time,

    @Valid
    @NotNull(message = "강습형태ID/강습구분ID는 null이 될 수 없습니다.")
    @Schema(description = "강습형태ID/강습구분ID")
    Type type,

    @Min(value = 0, message = "담당강사ID는 0 이상이어야 합니다.")
    @Schema(description = "담당강사", example = "1")
    long instructorId,

    @Valid
    @NotNull(message = "강습 티켓 리스트는 null이 될 수 없습니다.")
    @Schema(description = "강습 티켓 리스트")
    List<Ticket> tickets,

    @NotNull(message = "정원 정보는 null이 될 수 없습니다.")
    @Schema(description = "정원 정보")
    RegistrationCapacity registrationCapacity,

    @Schema(description = "사용자 노출 여부", example = "true")
    boolean isExposed
) {

  @Builder
  @Schema(name = "CreateBoSwimmingClassRequestType")
  record Type(
      @Min(value = 0, message = "강습형태ID는 0 이상이어야 합니다.")
      @Schema(description = "강습형태ID", example = "1")
      long classTypeId,

      @Min(value = 0, message = "강습구분ID는 0 이상이어야 합니다.")
      @Schema(description = "강습구분ID", example = "1")
      long classSubTypeId
  ) {

  }

  @Builder
  record Time(
      @NotNull(message = "강습 시작 시간은 null이 될 수 없습니다.")
      @Schema(description = "강습 시작 시간", example = "06:00")
      LocalTime startTime,

      @NotNull(message = "강습 종료 시간은 null이 될 수 없습니다.")
      @Schema(description = "강습 종료 시간", example = "06:50")
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

  public CreateBoSwimmingClassCommand toCommand(long swimmingPoolId) {
    return CreateBoSwimmingClassCommand.builder()
        .swimmingPoolId(swimmingPoolId)
        .month(this.month)
        .days(this.days)
        .time(
            CreateBoSwimmingClassCommand.Time.builder()
                .startTime(this.time.startTime)
                .endTime(this.time.endTime)
                .build()
        )
        .type(
            CreateBoSwimmingClassCommand.Type.builder()
                .classTypeId(this.type.classTypeId)
                .classSubTypeId(this.type.classSubTypeId)
                .build()
        )
        .instructorId(this.instructorId)
        .tickets(this.tickets.stream()
            .map(ticket ->
                CreateBoSwimmingClassCommand.Ticket.builder()
                    .name(ticket.name())
                    .price(ticket.price())
                    .build())
            .toList())
        .registrationCapacity(
            CreateBoSwimmingClassCommand.RegistrationCapacity.builder()
                .totalCapacity(this.registrationCapacity.totalCapacity)
                .reservationLimitCount(this.registrationCapacity.reservationLimitCount)
                .build()
        )
        .build();
  }
}
