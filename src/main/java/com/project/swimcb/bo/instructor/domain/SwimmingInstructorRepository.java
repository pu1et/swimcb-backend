package com.project.swimcb.bo.instructor.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingInstructorRepository extends JpaRepository<SwimmingInstructor, Long> {

  Optional<SwimmingInstructor> findById(long instructorId);
}
