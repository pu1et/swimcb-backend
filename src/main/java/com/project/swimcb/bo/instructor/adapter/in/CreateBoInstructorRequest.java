package com.project.swimcb.bo.instructor.adapter.in;

import com.project.swimcb.bo.instructor.domain.CreateBoInstructorCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateBoInstructorRequest(
    @NotNull(message = "강사 이름은 null이 될 수 없습니다.")
    String name
) {

  public CreateBoInstructorCommand toCommand(long swimmingPoolId) {
    return CreateBoInstructorCommand.builder()
        .swimmingPoolId(swimmingPoolId)
        .name(name)
        .build();
  }
}
