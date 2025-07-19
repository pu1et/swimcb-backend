package com.project.swimcb.favorite.domain;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

@Builder
public record FindFavoriteCondition(
    @NonNull Long memberId,
    @NonNull Double memberLatitude,
    @NonNull Double memberLongitude,
    FavoriteTargetType targetType,
    @NonNull Pageable pageable
) {

}
