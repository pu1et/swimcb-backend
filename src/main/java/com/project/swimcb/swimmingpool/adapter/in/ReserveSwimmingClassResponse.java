package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingClassReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(name = "수영 클래스 예약 응답")
public record ReserveSwimmingClassResponse(
    @Schema(description = "예약 상태", example = "NOT_RESERVABLE|WAITING_RESERVABLE|RESERVABLE")
    @NonNull SwimmingClassReservationStatus status,

    @Schema(description = "대기 번호", example = "1")
    Integer waitingNo
) {

}
