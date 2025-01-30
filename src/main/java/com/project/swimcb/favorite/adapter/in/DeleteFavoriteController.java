package com.project.swimcb.favorite.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "즐겨찾기")
@RestController
@RequestMapping("/api/favorites/{favoriteId}")
public class DeleteFavoriteController {

  @Operation(summary = "즐겨찾기 삭제")
  @DeleteMapping
  public void deleteFavorite(@PathVariable(value = "favoriteId") long favoriteId) {
  }
}
