package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.AdminEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

  Optional<AdminEntity> findByLoginId(@NonNull String loginId);
}
