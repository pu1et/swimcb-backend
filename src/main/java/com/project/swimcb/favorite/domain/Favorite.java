package com.project.swimcb.favorite.domain;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;

public sealed interface Favorite permits
    SwimmingPoolFavorite,
    SwimmingClassFavorite,
    FreeSwimmingFavorite {

  Long id();

  Long targetId();

  FavoriteTargetType targetType();

}
