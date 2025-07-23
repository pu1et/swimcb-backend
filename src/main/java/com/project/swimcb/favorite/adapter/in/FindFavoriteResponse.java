package com.project.swimcb.favorite.adapter.in;


import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Page;


public record FindFavoriteResponse(
    @NonNull Page<Favorite> contents
) {

  @Schema(oneOf = {
      SwimmingPoolFavorite.class,
      SwimmingClassFavorite.class,
      FreeSwimmingFavorite.class
  })
  public sealed interface Favorite permits
      SwimmingPoolFavorite,
      SwimmingClassFavorite,
      FreeSwimmingFavorite {

    Long id();

    FavoriteTargetType targetType();

    Long targetId();

  }

  @Builder
  record SwimmingPoolFavorite(

      @NonNull
      @Schema(example = "1")
      Long id,

      @NonNull
      @Schema(example = "1")
      Long targetId,

      @NonNull
      @Schema(example = "SWIMMING_POOL")
      FavoriteTargetType targetType,

      @NonNull
      @Schema(example = "https://example.com/image.jpg")
      String imageUrl,

      @NonNull
      @Schema(example = "500")
      Integer distance,

      @NonNull
      @Schema(example = "서울시립수영장")
      String name,

      @NonNull
      @Schema(example = "서울특별시 중구 세종대로 110")
      String address,

      @NonNull
      @Schema(example = "4.5")
      Double rating,

      @NonNull
      @Schema(example = "10")
      Integer reviewCount

  ) implements Favorite {

  }

  @Builder
  record SwimmingClassFavorite(

      @NonNull
      @Schema(example = "1")
      Long id,

      @NonNull
      @Schema(example = "1")
      Long targetId,

      @NonNull
      @Schema(example = "SWIMMING_CLASS")
      FavoriteTargetType targetType,

      @NonNull
      @Schema(example = "1")
      Long swimmingPoolId,

      @NonNull
      @Schema(example = "올림픽 수영장")
      String swimmingPoolName,

      @NonNull
      @Schema(example = "4")
      Integer month,

      @NonNull
      @Schema(example = "1")
      Long typeId,

      @NonNull
      @Schema(example = "단체강습")
      String typeName,

      @NonNull
      @Schema(example = "1")
      Long subTypeId,

      @NonNull
      @Schema(example = "기초")
      String subTypeName,

      @NonNull
      @Schema(example = "[월|화|수|목|금|토|일]")
      List<String> days,

      @NonNull
      @Schema(example = "06:00:00")
      LocalTime startTime,

      @NonNull
      @Schema(example = "07:00:00")
      LocalTime endTime,

      @NonNull
      @Schema(example = "10000")
      Integer minTicketPrice

  ) implements Favorite {

  }

  @Builder
  record FreeSwimmingFavorite(

      @NonNull
      @Schema(example = "1")
      Long id,

      @NonNull
      @Schema(example = "1")
      Long targetId,

      @NonNull
      @Schema(example = "FREE_SWIMMING")
      FavoriteTargetType targetType,

      @NonNull
      @Schema(example = "1")
      Long swimmingPoolId,

      @NonNull
      @Schema(example = "올림픽 수영장")
      String swimmingPoolName,

      @NonNull
      @Schema(example = "2025-03-01")
      LocalDate date,

      @NonNull
      @Schema(example = "06:00:00")
      LocalTime startTime,

      @NonNull
      @Schema(example = "07:00:00")
      LocalTime endTime,

      @NonNull
      @Schema(example = "10000")
      Integer minTicketPrice

  ) implements Favorite {

  }

}
