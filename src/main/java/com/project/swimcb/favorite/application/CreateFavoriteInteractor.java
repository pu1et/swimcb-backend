package com.project.swimcb.favorite.application;

import com.project.swimcb.db.entity.FavoriteEntity;
import com.project.swimcb.db.repository.FavoriteRepository;
import com.project.swimcb.favorite.adapter.in.CreateFavoriteCommand;
import com.project.swimcb.favorite.application.in.CreateFavoriteUseCase;
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

  @Override
  public void createFavorite(@NonNull CreateFavoriteCommand command) {
    val favorite = FavoriteEntity.of(command.memberId(), command.targetType(),
        command.targetId());
    favoriteRepository.save(favorite);
  }

}
