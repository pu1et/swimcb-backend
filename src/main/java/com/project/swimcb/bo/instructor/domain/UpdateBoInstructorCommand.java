package com.project.swimcb.bo.instructor.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateBoInstructorCommand(
    long swimmingPoolId,
    long instructorId,
    @NonNull String name
) {

}
