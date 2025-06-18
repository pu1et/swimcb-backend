package com.project.swimcb.bo.admin.domain;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

  Optional<Admin> findByLoginId(@NonNull String loginId);
}
