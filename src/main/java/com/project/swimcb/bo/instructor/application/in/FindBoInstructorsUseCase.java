package com.project.swimcb.bo.instructor.application.in;

import com.project.swimcb.bo.instructor.adapter.in.FindBoInstructorsResponse;

public interface FindBoInstructorsUseCase {

  FindBoInstructorsResponse findBoInstructors(long swimmingPoolId);
}
