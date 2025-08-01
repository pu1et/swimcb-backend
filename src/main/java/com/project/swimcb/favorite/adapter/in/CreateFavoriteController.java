package com.project.swimcb.favorite.adapter.in;

import com.project.swimcb.favorite.application.in.CreateFavoriteUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "즐겨찾기")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class CreateFavoriteController {

  private final CreateFavoriteUseCase useCase;

  @Operation(summary = "즐겨찾기 등록")
  @PostMapping
  public void createFavorite(
      @Valid @RequestBody CreateFavoriteRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.createFavorite(request.toCommand(tokenInfo.memberId()));
  }

}
