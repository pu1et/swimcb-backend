package com.project.swimcb.bo.instructor.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingInstructorRepository extends JpaRepository<SwimmingInstructor, Long> {

  Optional<SwimmingInstructor> findById(long instructorId);
  Optional<SwimmingInstructor> findBySwimmingPool_IdAndId(long swimmingPoolId, long instructorId);
  List<SwimmingInstructor> findAllBySwimmingPool_Id(long swimmingPoolId);
}
