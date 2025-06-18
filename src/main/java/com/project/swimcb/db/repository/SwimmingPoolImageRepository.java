package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SwimmingPoolImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingPoolImageRepository extends JpaRepository<SwimmingPoolImageEntity, Long> {

  List<SwimmingPoolImageEntity> findBySwimmingPoolId(long swimmingPoolId);

  void deleteAllBySwimmingPoolId(long swimmingPoolId);
}
