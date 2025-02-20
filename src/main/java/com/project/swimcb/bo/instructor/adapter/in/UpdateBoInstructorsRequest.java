package com.project.swimcb.bo.instructor.adapter.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateBoInstructorsRequest(
    @Valid
    @NotNull(message = "강사 리스트는 null이 될 수 없습니다.")
    List<Instructor> instructors
) {

  @Builder
  record Instructor(
      Long instructorId,

      @NotNull(message = "강사 이름은 null이 될 수 없습니다.")
      String name
  ) {

  }
}
