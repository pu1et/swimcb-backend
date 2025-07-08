package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingReservationBlockedCommand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SetFreeSwimmingReservationBlockedRequest(

    @NotEmpty(message = "자유수영 일별 상태 ID 리스트는 필수입니다.")
    List<Long> freeSwimmingDayStatusIds,

    @NotNull(message = "예약마감 여부는 null이 될 수 없습니다.")
    Boolean isReservationBlocked
) {

  public SetFreeSwimmingReservationBlockedCommand toCommand(@NonNull Long swimmingPoolId) {
    return SetFreeSwimmingReservationBlockedCommand.builder()
        .swimmingPoolId(swimmingPoolId)
        .freeSwimmingDayStatusIds(freeSwimmingDayStatusIds)
        .isReservationBlocked(isReservationBlocked)
        .build();
  }

}
