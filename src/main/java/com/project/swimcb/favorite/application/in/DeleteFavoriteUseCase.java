package com.project.swimcb.favorite.application.in;

import lombok.NonNull;

public interface DeleteFavoriteUseCase {

  void deleteFavorite(@NonNull Long memberId, @NonNull Long favoriteId);

}
