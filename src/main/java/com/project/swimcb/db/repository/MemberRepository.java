package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.MemberEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByEmail(@NonNull String email);
}
