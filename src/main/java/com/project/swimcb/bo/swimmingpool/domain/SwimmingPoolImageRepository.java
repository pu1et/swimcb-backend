package com.project.swimcb.bo.swimmingpool.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingPoolImageRepository extends JpaRepository<SwimmingPoolImage, Long> {

  List<SwimmingPoolImage> findBySwimmingPoolId(long swimmingPoolId);

  void deleteAllBySwimmingPoolId(long swimmingPoolId);
}
