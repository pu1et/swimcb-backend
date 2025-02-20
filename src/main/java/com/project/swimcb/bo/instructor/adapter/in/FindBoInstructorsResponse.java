package com.project.swimcb.bo.instructor.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoInstructorsResponse(
    @NonNull List<Instructor> instructors
) {

  @Builder
  record Instructor(
      long instructorId,
      @NonNull String name
  ) {

  }
}
