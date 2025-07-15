package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.FavoriteEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

  Optional<FavoriteEntity> findByMember_IdAndId(long memberId, long favoriteId);

}
