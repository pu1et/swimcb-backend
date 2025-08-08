package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.FavoriteEntity;
import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

  Optional<FavoriteEntity> findByMember_IdAndId(long memberId, long favoriteId);

  Optional<FavoriteEntity> findByMember_IdAndTargetTypeAndTargetId(
      long memberId, @NonNull FavoriteTargetType targetType, long targetId
  );

}
