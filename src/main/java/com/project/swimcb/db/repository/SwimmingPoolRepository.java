package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SwimmingPoolEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingPoolRepository extends JpaRepository<SwimmingPoolEntity, Long> {

  Optional<SwimmingPoolEntity> findByAdmin_Id(@NonNull Long adminId);

}
