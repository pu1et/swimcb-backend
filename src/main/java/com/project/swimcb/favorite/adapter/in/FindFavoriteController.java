package com.project.swimcb.favorite.adapter.in;

import com.project.swimcb.favorite.domain.FindFavoriteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "즐겨찾기")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/favorites")
public class FindFavoriteController {

  @Operation(summary = "즐겨찾기 조회")
  @GetMapping
  public FindFavoriteResponse findFavorites() {
    return null;
  }

}
