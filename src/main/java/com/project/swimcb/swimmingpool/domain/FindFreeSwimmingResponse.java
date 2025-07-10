package com.project.swimcb.swimmingpool.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
@Schema(name = "FindFreeSwimmingResponse")
public record FindFreeSwimmingResponse(
    @NonNull List<FreeSwimming> freeSwimmings
) {

  @Builder
  @Schema(name = "FindFreeSwimmingResponse.FreeSwimming")
  public record FreeSwimming(
      @NonNull
      @Schema(description = "수영장 ID", example = "1")
      Long swimmingPoolId,

      @NonNull
      @Schema(description = "수영장 이미지 URL", example = "https://example.com/image.jpg")
      String imageUrl,

      @NonNull
      @Schema(description = "즐겨찾기 여부", example = "true")
      Boolean isFavorite,

      @NonNull
      @Schema(description = "거리", example = "500")
      Integer distance,

      @NonNull
      @Schema(description = "수영장 이름", example = "서울시립수영장")
      String name,

      @NonNull
      @Schema(description = "수영장 주소", example = "서울특별시 중구 세종대로 110")
      String address,

      @NonNull
      @Schema(description = "수영장 평점", example = "4.5")
      Double rating,

      @NonNull
      @Schema(description = "리뷰 수", example = "10")
      Integer reviewCount
  ) {

  }

}
