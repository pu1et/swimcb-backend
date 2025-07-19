package com.project.swimcb.favorite.application.in;

import com.project.swimcb.favorite.domain.Favorite;
import com.project.swimcb.favorite.domain.FindFavoriteCondition;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface FindFavoriteUseCase {

  Page<Favorite> findFavorites(@NonNull FindFavoriteCondition condition);

}
