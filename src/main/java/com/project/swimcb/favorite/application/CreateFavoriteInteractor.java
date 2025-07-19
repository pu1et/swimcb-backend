package com.project.swimcb.favorite.application;

import com.project.swimcb.db.entity.FavoriteEntity;
import com.project.swimcb.db.repository.FavoriteRepository;
import com.project.swimcb.db.repository.FreeSwimmingDayStatusRepository;
import com.project.swimcb.db.repository.SwimmingClassRepository;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
import com.project.swimcb.favorite.adapter.in.CreateFavoriteCommand;
import com.project.swimcb.favorite.application.in.CreateFavoriteUseCase;
import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CreateFavoriteInteractor implements CreateFavoriteUseCase {

  private final FavoriteRepository favoriteRepository;
  private final SwimmingPoolRepository swimmingPoolRepository;
  private final SwimmingClassRepository swimmingClassRepository;
  private final FreeSwimmingDayStatusRepository freeSwimmingDayStatusRepository;

  @Override
  public void createFavorite(@NonNull CreateFavoriteCommand command) {

    throwIfDoesNotExist(command.targetType(), command.targetId());

    val favorite = FavoriteEntity.of(command.memberId(), command.targetType(),
        command.targetId());
    favoriteRepository.save(favorite);
  }

  private void throwIfDoesNotExist(
      @NonNull FavoriteTargetType favoriteTargetType,
      @NonNull Long targetId
  ) {

    switch (favoriteTargetType) {
      case SWIMMING_POOL -> swimmingPoolRepository.findById(targetId)
          .orElseThrow(() -> new IllegalArgumentException("해당 수영장 정보가 존재하지 않습니다."));
      case SWIMMING_CLASS -> swimmingClassRepository.findById(targetId)
          .orElseThrow(() -> new IllegalArgumentException("해당 수영강습 정보가 존재하지 않습니다."));
      case FREE_SWIMMING -> freeSwimmingDayStatusRepository.findById(targetId)
          .orElseThrow(() -> new IllegalArgumentException("해당 자유수영 정보가 존재하지 않습니다."));
    }
  }

}
