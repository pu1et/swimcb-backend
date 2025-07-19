package com.project.swimcb.favorite.application.out;

import com.project.swimcb.favorite.domain.Favorite;
import com.project.swimcb.favorite.domain.FindFavoriteCondition;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface FindFavoriteDsGateway {

  Page<Favorite> findFavorites(@NonNull FindFavoriteCondition condition);

}
