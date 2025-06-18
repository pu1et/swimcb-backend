package com.project.swimcb.bo.instructor.application;

import com.project.swimcb.bo.instructor.application.in.UpdateBoInstructorUseCase;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.project.swimcb.bo.instructor.domain.UpdateBoInstructorCommand;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateBoInstructorInteractor implements UpdateBoInstructorUseCase {

  private final SwimmingInstructorRepository swimmingInstructorRepository;

  @Override
  public void updateBoInstructor(@NonNull UpdateBoInstructorCommand request) {
    val swimmingInstructor = swimmingInstructorRepository.findBySwimmingPool_IdAndId(
            request.swimmingPoolId(), request.instructorId())
        .orElseThrow(() -> new NoSuchElementException("강사가 존재하지 않습니다."));

    swimmingInstructor.updateName(request.name());
  }
}
