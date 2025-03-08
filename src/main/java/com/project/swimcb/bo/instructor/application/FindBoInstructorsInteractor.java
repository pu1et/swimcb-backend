package com.project.swimcb.bo.instructor.application;

import com.project.swimcb.bo.instructor.adapter.in.FindBoInstructorsResponse;
import com.project.swimcb.bo.instructor.adapter.in.FindBoInstructorsResponse.Instructor;
import com.project.swimcb.bo.instructor.application.in.FindBoInstructorsUseCase;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindBoInstructorsInteractor implements FindBoInstructorsUseCase {

  private final SwimmingInstructorRepository swimmingInstructorRepository;

  @Override
  public FindBoInstructorsResponse findBoInstructors(long swimmingPoolId) {
    val instructorEntities = swimmingInstructorRepository.findAllBySwimmingPool_Id(swimmingPoolId);

    val instructors = instructorEntities.stream()
        .map(i -> Instructor.builder()
            .instructorId(i.getId())
            .name(i.getName())
            .build())
        .toList();
    return new FindBoInstructorsResponse(instructors);
  }
}
