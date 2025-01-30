package com.project.swimcb.favorite.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "즐겨찾기")
@RestController
@RequestMapping("/api/favorite")
public class RegisterFavoriteController {

  @Operation(summary = "즐겨찾기 등록")
  @PostMapping
  public void registerFavorite(@RequestBody RegisterFavoriteRequest request) {

  }
}
