package com.project.swimcb.favorite.adapter.in;

import com.project.swimcb.favorite.application.in.DeleteFavoriteCommand;
import com.project.swimcb.favorite.application.in.DeleteFavoriteUseCase;
import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "즐겨찾기")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/favorites/{targetType}/{targetId}")
@RequiredArgsConstructor
public class DeleteFavoriteController {

  private final DeleteFavoriteUseCase useCase;

  @Operation(summary = "즐겨찾기 삭제")
  @DeleteMapping
  public void deleteFavorite(
      @PathVariable(value = "targetType") FavoriteTargetType targetType,
      @PathVariable(value = "targetId") long targetId,

      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.deleteFavorite(
        DeleteFavoriteCommand.builder()
            .memberId(tokenInfo.memberId())
            .targetType(targetType)
            .targetId(targetId)
            .build()
    );
  }

}
