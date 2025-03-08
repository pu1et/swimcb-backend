package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassSubTypeCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateBoSwimmingClassSubTypeRequest(
    @NotNull(message = "강습구분 이름은 null이 될 수 없습니다.")
    @Schema(description = "강습구분 이름", example = "기초")
    String name
) {


  public @NonNull UpdateBoSwimmingClassSubTypeCommand toCommand(long swimmingPoolId,
      long swimmingClassTypeId, long swimmingClassSubTypeId) {
    return UpdateBoSwimmingClassSubTypeCommand.builder()
        .swimmingPoolId(swimmingPoolId)
        .swimmingClassTypeId(swimmingClassTypeId)
        .swimmingClassSubTypeId(swimmingClassSubTypeId)
        .name(name)
        .build();
  }
}
