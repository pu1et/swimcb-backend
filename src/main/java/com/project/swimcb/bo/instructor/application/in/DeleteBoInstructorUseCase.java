package com.project.swimcb.bo.instructor.application.in;

import com.project.swimcb.bo.instructor.domain.DeleteBoInstructorCommand;
import lombok.NonNull;

public interface DeleteBoInstructorUseCase {

  void deleteBoInstructor(@NonNull DeleteBoInstructorCommand request);
}
