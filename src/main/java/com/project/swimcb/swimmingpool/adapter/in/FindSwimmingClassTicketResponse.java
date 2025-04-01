package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingClassReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
@Schema(name = "수영 클래스 예약 - 티켓 정보 조회 응답")
public record FindSwimmingClassTicketResponse(
    @NonNull SwimmingClass swimmingClass,
    @NonNull SwimmingClassTicket ticket
) {

  @Builder
  @Schema(name = "수영 클래스 예약 - 수영 클래스 정보")
  public record SwimmingClass(
      @Schema(description = "월", example = "1")
      int month,

      @Schema(description = "강습형태", example = "단체강습|아동수영|아쿠아로빅|특별반|레슨")
      @NonNull String type,

      @Schema(description = "강습구분", example = "기초")
      @NonNull String subType,

      @Schema(description = "요일", example = "월|화|수|목|금|토|일")
      @NonNull List<String> days,

      @Schema(description = "시작 시간", example = "10:00:00")
      @NonNull LocalTime startTime,

      @Schema(description = "종료 시간", example = "11:00:00")
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  @Schema(name = "수영 클래스 예약 - 티켓 정보")
  public record SwimmingClassTicket(

      @Schema(description = "이름", example = "1")
      @NonNull String name,

      @Schema(description = "가격", example = "10000")
      int price,

      @Schema(description = "예약 상태", example = "NOT_RESERVABLE|WAITING_RESERVABLE|RESERVABLE")
      @NonNull SwimmingClassReservationStatus status
  ) {

  }
}
