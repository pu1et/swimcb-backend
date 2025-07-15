package com.project.swimcb.favorite.adapter.in;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateFavoriteRequest")
public record CreateFavoriteRequest(

    @NotNull(message = "targetType은 null일 수 없습니다.")
    @Schema(description = "즐겨찾기 타입", example = "[SWIMMING_POOL, SWIMMING_CLASS, FREE_SWIMMING]")
    FavoriteTargetType targetType,

    @NotNull(message = "targetId는 null일 수 없습니다.")
    @Schema(description = "즐겨찾기 대상 ID", example = "1")
    Long targetId
) {

}
