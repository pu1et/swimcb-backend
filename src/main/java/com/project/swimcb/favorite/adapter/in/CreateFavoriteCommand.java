package com.project.swimcb.favorite.adapter.in;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateFavoriteCommand(
    @NonNull Long memberId,
    @NonNull FavoriteTargetType targetType,
    @NonNull Long targetId
) {

}
