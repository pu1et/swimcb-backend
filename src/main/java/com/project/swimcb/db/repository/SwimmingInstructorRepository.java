package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SwimmingInstructorEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingInstructorRepository extends JpaRepository<SwimmingInstructorEntity, Long> {

  Optional<SwimmingInstructorEntity> findById(long instructorId);
  Optional<SwimmingInstructorEntity> findBySwimmingPool_IdAndId(long swimmingPoolId, long instructorId);
  List<SwimmingInstructorEntity> findAllBySwimmingPool_Id(long swimmingPoolId);
  int deleteBySwimmingPool_IdAndId(long swimmingPoolId, long instructorId);
}
