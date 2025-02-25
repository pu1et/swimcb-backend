package com.project.swimcb.favorite.adapter.in;

import com.project.swimcb.favorite.domain.Favorite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
  public List<Favorite> findFavorites() {
    return List.of(
        Favorite.builder()
            .favoriteId(1L)
            .swimmingPoolId(1L)
            .imageUrl("https://ibb.co/bjGKF8WV")
            .distance("400m")
            .flag("클래스")
            .registrationInfo("수영 강습반 등록은 등록 기간 내 선착순으로 접수합니다.")
            .isFavorite(true)
            .name("올림픽 수영장")
            .address("서울시 송파구 올림픽로")
            .star("4.5")
            .reviewCount(35)
            .build(),
        Favorite.builder()
            .favoriteId(2L)
            .swimmingPoolId(2L)
            .imageUrl("https://ibb.co/bjGKF8WV")
            .distance("400m")
            .flag("자유수영")
            .registrationInfo("수영 강습반 등록은 등록 기간 내 선착순으로 접수합니다.")
            .isFavorite(true)
            .name("올림픽 수영장")
            .address("서울시 송파구 올림픽로")
            .star("4.5")
            .reviewCount(35)
            .build(),
        Favorite.builder()
            .favoriteId(3L)
            .swimmingPoolId(3L)
            .imageUrl("https://ibb.co/zWdrk20p")
            .distance("970m")
            .flag("클래스")
            .registrationInfo("수영 강습반 등록은 등록 기간 내 선착순으로 접수합니다.")
            .isFavorite(true)
            .name("웨일즈수영장")
            .address("서울시 송파구 백제고분로")
            .star("4.1")
            .reviewCount(9)
            .build()
    );
  }
}
