package com.project.swimcb.favorite.application;

import com.project.swimcb.favorite.application.in.FindFavoriteUseCase;
import com.project.swimcb.favorite.application.out.FindFavoriteDsGateway;
import com.project.swimcb.favorite.domain.Favorite;
import com.project.swimcb.favorite.domain.FindFavoriteCondition;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindFavoriteInteractor implements FindFavoriteUseCase {

  private final FindFavoriteDsGateway gateway;

  @Override
  public Page<Favorite> findFavorites(@NonNull FindFavoriteCondition condition) {
    return gateway.findFavorites(condition);
  }

}
