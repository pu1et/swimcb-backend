package com.project.swimcb.favorite.application;

import com.project.swimcb.db.repository.FavoriteRepository;
import com.project.swimcb.favorite.application.in.DeleteFavoriteUseCase;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteFavoriteInteractor implements DeleteFavoriteUseCase {

  private final FavoriteRepository repository;

  @Override
  public void deleteFavorite(@NonNull Long memberId, @NonNull Long favoriteId) {
    repository.findByMember_IdAndId(memberId, favoriteId)
        .ifPresentOrElse(favorite -> repository.deleteById(favorite.getId()),
            () -> {
              throw new NoSuchElementException(
                  "즐겨찾기를 찾을 수 없습니다. memberId: " + memberId + ", favoriteId: " + favoriteId);
            });
  }

}
