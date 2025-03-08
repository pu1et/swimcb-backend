package com.project.swimcb.bo.instructor.adapter.in;

import com.project.swimcb.bo.instructor.domain.UpdateBoInstructorCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateBoInstructorRequest(
    @NotNull(message = "강사 이름은 null이 될 수 없습니다.")
    String name
) {

  public UpdateBoInstructorCommand toCommand(long swimmingPoolId, long instructorId) {
    return UpdateBoInstructorCommand.builder()
        .swimmingPoolId(swimmingPoolId)
        .instructorId(instructorId)
        .name(name)
        .build();
  }
}
