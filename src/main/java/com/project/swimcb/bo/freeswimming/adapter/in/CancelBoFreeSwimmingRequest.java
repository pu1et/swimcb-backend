package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.domain.CancelBoFreeSwimmingCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public record CancelBoFreeSwimmingRequest(
    @NotNull(message = "폐강 사유는 필수입니다.")
    @Schema(description = "폐강 사유", example = "강사 사정으로 인한 폐강")
    String cancelReason
) {

  public CancelBoFreeSwimmingCommand toCommand(
      @NonNull Long swimmingPoolId,
      @NonNull Long freeSwimmingId
  ) {
    return CancelBoFreeSwimmingCommand.builder()
        .swimmingPoolId(swimmingPoolId)
        .freeSwimmingId(freeSwimmingId)
        .cancelReason(cancelReason)
        .build();
  }

}
