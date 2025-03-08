package com.project.swimcb.bo.instructor.application.in;

import com.project.swimcb.bo.instructor.domain.UpdateBoInstructorCommand;
import lombok.NonNull;

public interface UpdateBoInstructorUseCase {

  void updateBoInstructor(@NonNull UpdateBoInstructorCommand request);
}
