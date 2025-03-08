package com.project.swimcb.bo.instructor.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateBoInstructorCommand(
    long swimmingPoolId,
    @NonNull String name
) {

}
