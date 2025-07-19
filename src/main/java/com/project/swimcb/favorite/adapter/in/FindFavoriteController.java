package com.project.swimcb.favorite.adapter.in;

import com.project.swimcb.favorite.application.in.FindFavoriteUseCase;
import com.project.swimcb.favorite.domain.FindFavoriteCondition;
import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "즐겨찾기")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FindFavoriteController {

  private final FindFavoriteUseCase useCase;
  private final FindFavoriteResponseMapper responseMapper;

  @Operation(summary = "즐겨찾기 조회")
  @GetMapping
  public FindFavoriteResponse findFavorites(
      @Parameter(description = "위도", example = "37.5665") @RequestParam(value = "memberLatitude") double memberLatitude,
      @Parameter(description = "경도", example = "126.9789") @RequestParam(value = "memberLongitude") double memberLongitude,
      @Parameter(description = "즐겨찾기 타입", example = "SWIMMING_POOL") @RequestParam(value = "targetType", required = false) FavoriteTargetType targetType,
      @Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page") int page,
      @Parameter(description = "페이지 크기", example = "10") @RequestParam(value = "size") int size,

      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    val result = useCase.findFavorites(FindFavoriteCondition.builder()
        .memberId(tokenInfo.memberId())
        .memberLatitude(memberLatitude)
        .memberLongitude(memberLongitude)
        .targetType(targetType)
        .pageable(PageRequest.of(page - 1, size))
        .build()
    );
    return responseMapper.mapToResponse(result);
  }

}
