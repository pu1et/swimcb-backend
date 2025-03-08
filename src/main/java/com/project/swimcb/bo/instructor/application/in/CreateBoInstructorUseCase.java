package com.project.swimcb.bo.instructor.application.in;

import com.project.swimcb.bo.instructor.domain.CreateBoInstructorCommand;
import lombok.NonNull;

public interface CreateBoInstructorUseCase {

  void createBoInstructor(@NonNull CreateBoInstructorCommand request);
}
