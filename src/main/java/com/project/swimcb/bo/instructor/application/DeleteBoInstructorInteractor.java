package com.project.swimcb.bo.instructor.application;

import com.project.swimcb.bo.instructor.application.in.DeleteBoInstructorUseCase;
import com.project.swimcb.bo.instructor.domain.DeleteBoInstructorCommand;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteBoInstructorInteractor implements DeleteBoInstructorUseCase {

  private final SwimmingInstructorRepository swimmingInstructorRepository;

  @Override
  public void deleteBoInstructor(@NonNull DeleteBoInstructorCommand request) {
    val count = swimmingInstructorRepository.deleteBySwimmingPool_IdAndId(
        request.swimmingPoolId(), request.instructorId());
    if (count != 1) {
      throw new NoSuchElementException("강사가 존재하지 않습니다.");
    }
  }
}
