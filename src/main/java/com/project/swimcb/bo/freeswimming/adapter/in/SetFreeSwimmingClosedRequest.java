package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingClosedCommand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record SetFreeSwimmingClosedRequest(

    @NotEmpty(message = "자유수영 일별 상태 ID 리스트는 필수입니다.")
    List<Long> freeSwimmingDayStatusIds,

    @NotNull(message = "휴무일 여부는 null이 될 수 없습니다.")
    Boolean isClosed
) {

  public SetFreeSwimmingClosedCommand toCommand(@NotNull Long swimmingPoolId) {
    return SetFreeSwimmingClosedCommand.builder()
        .swimmingPoolId(swimmingPoolId)
        .freeSwimmingDayStatusIds(freeSwimmingDayStatusIds)
        .isClosed(isClosed)
        .build();
  }

}
