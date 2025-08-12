package com.project.swimcb.favorite.application.in;

import lombok.NonNull;

public interface DeleteFavoriteUseCase {

  void deleteFavorite(@NonNull DeleteFavoriteCommand command);

}
