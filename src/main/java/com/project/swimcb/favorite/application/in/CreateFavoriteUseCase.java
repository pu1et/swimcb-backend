package com.project.swimcb.favorite.application.in;

import com.project.swimcb.favorite.adapter.in.CreateFavoriteCommand;
import lombok.NonNull;

public interface CreateFavoriteUseCase {

  void createFavorite(@NonNull CreateFavoriteCommand command);
}
