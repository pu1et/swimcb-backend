package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.domain.enums.UpdateBoSwimmingClassesSwimmingClassType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "BO 클래스 데이터 관리 - 클래스 일괄 수정 response")
public record UpdateBoSwimmingClassRequest(
    @Valid
    @NotNull(message = "클래스는 null이 될 수 없습니다.")
    @Schema(description = "수정할 클래스")
    SwimmingClass swimmingClass
) {

  @Builder
  record SwimmingClass(

      @Schema(description = "클래스 ID", example = "1")
      long id,

      @NotNull(message = "강습 요일은 null이 될 수 없습니다.")
      @Schema(description = "강습 요일")
      Days days,

      @Valid
      @NotNull(message = "강습 시작/종료 시간은 null이 될 수 없습니다.")
      @Schema(description = "강습 시작/종료 시간")
      Time time,

      @Valid
      @NotNull(message = "강습 형태/구분은 null이 될 수 없습니다.")
      @Schema(description = "강습 형태/구분")
      Type type,

      @NotNull(message = "담당강사는 null이 될 수 없습니다.")
      @Schema(description = "담당강사", example = "손지혜")
      String instructorName,

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

  }

  @Builder
  record Days(
      @Schema(description = "월요일 수업 여부", example = "true")
      boolean isMonday,
      @Schema(description = "화요일 수업 여부", example = "false")
      boolean isTuesday,
      @Schema(description = "수요일 수업 여부", example = "true")
      boolean isWednesday,
      @Schema(description = "목요일 수업 여부", example = "false")
      boolean isThursday,
      @Schema(description = "금요일 수업 여부", example = "false")
      boolean isFriday,
      @Schema(description = "토요일 수업 여부", example = "false")
      boolean isSaturday,
      @Schema(description = "일요일 수업 여부", example = "false")
      boolean isSunday
  ) {

  }

  @Builder
  record Type(
      @NotNull(message = "강습 형태는 null이 될 수 없습니다.")
      @Schema(description = "강습 형태", example = "GROUP")
      UpdateBoSwimmingClassesSwimmingClassType type,

      @NotNull(message = "강습 구분은 null이 될 수 없습니다.")
      @Schema(description = "강습 구분", example = "마스터즈")
      String subType
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
}
