package com.project.swimcb.bo.swimmingpool.domain;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingPoolRepository extends JpaRepository<SwimmingPool, Long> {

  Optional<SwimmingPool> findByAdmin_Id(@NonNull Long adminId);

}
