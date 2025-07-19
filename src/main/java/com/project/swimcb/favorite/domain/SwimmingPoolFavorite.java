package com.project.swimcb.favorite.domain;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolFavorite(

    @NonNull
    Long targetId,

    @NonNull
    FavoriteTargetType targetType,

    @NonNull
    String imagePath,

    @NonNull
    Integer distance,

    @NonNull
    String name,

    @NonNull
    String address,

    Double rating,

    Long reviewCount

) implements Favorite {

}
