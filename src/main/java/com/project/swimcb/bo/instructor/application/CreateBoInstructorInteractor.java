package com.project.swimcb.bo.instructor.application;

import com.project.swimcb.bo.instructor.application.in.CreateBoInstructorUseCase;
import com.project.swimcb.bo.instructor.domain.CreateBoInstructorCommand;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructor;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolRepository;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateBoInstructorInteractor implements CreateBoInstructorUseCase {

  private final SwimmingPoolRepository swimmingPoolRepository;
  private final SwimmingInstructorRepository swimmingInstructorRepository;

  @Override
  public void createBoInstructor(@NonNull CreateBoInstructorCommand request) {
    val swimmingPool = swimmingPoolRepository.findById(request.swimmingPoolId())
        .orElseThrow(() -> new NoSuchElementException("수영장이 존재하지 않습니다."));

    val instructor = SwimmingInstructor.create(swimmingPool, request.name());
    swimmingInstructorRepository.save(instructor);
  }
}
