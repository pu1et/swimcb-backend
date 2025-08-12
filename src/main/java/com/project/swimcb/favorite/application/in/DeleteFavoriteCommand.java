package com.project.swimcb.favorite.application.in;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record DeleteFavoriteCommand(
    @NonNull Long memberId,
    @NonNull FavoriteTargetType targetType,
    @NonNull Long targetId
) {

}
