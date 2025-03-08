package com.project.swimcb.bo.instructor.domain;

import lombok.Builder;

@Builder
public record DeleteBoInstructorCommand(
    long swimmingPoolId,
    long instructorId
) {

}
