package com.project.swimcb.bo.swimmingclass.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoSwimmingClassInstructorsResponse(
    @NonNull List<Instructor> instructors
) {

  @Builder
  record Instructor(
      long instructorId,
      @NonNull String name
  ) {

  }
}
