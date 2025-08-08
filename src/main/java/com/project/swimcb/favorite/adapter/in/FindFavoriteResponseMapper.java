package com.project.swimcb.favorite.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.favorite.domain.Favorite;
import com.project.swimcb.favorite.domain.FreeSwimmingFavorite;
import com.project.swimcb.favorite.domain.SwimmingClassFavorite;
import com.project.swimcb.favorite.domain.SwimmingPoolFavorite;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindFavoriteResponseMapper {

  private final ImageUrlPort imageUrlPort;

  public FindFavoriteResponse mapToResponse(@NonNull Page<Favorite> favorites) {
    val content = favorites.getContent()
        .stream()
        .map(i -> switch (i.targetType()) {
          case SWIMMING_POOL -> mapToSwimmingPoolFavorite((SwimmingPoolFavorite) i);
          case SWIMMING_CLASS -> mapToSwimmingClassFavorite((SwimmingClassFavorite) i);
          case FREE_SWIMMING -> mapToFreeSwimmingFavorite((FreeSwimmingFavorite) i);
        })
        .toList();

    return new FindFavoriteResponse(new PageImpl<>(content, favorites.getPageable(),
        favorites.getTotalElements()));
  }

  private FindFavoriteResponse.Favorite mapToSwimmingPoolFavorite(
      @NonNull SwimmingPoolFavorite i) {
    return FindFavoriteResponse.SwimmingPoolFavorite.builder()
        .id(i.id())
        .targetId(i.targetId())
        .targetType(i.targetType())
        .imageUrl(imageUrl(i.imagePath()))
        .distance(i.distance())
        .name(i.name())
        .address(i.address())
        .rating(Optional.ofNullable(i.rating()).orElse(0.0))
        .reviewCount(Optional.ofNullable(i.reviewCount()).map(Long::intValue).orElse(0))
        .build();
  }

  private FindFavoriteResponse.Favorite mapToSwimmingClassFavorite(
      @NonNull SwimmingClassFavorite i) {
    return FindFavoriteResponse.SwimmingClassFavorite.builder()
        .id(i.id())
        .targetId(i.targetId())
        .targetType(i.targetType())
        .swimmingPoolId(i.swimmingPoolId())
        .swimmingPoolName(i.swimmingPoolName())
        .month(i.month())
        .typeId(i.typeId())
        .typeName(i.typeName().getDescription())
        .subTypeId(i.subTypeId())
        .subTypeName(i.subTypeName())
        .days(i.daysOfWeek().toDays())
        .startTime(i.startTime())
        .endTime(i.endTime())
        .minTicketPrice(i.minTicketPrice())
        .isClosed(i.isClosed())
        .build();
  }

  private FindFavoriteResponse.Favorite mapToFreeSwimmingFavorite(
      @NonNull FreeSwimmingFavorite i) {
    return FindFavoriteResponse.FreeSwimmingFavorite.builder()
        .id(i.id())
        .targetId(i.targetId())
        .targetType(i.targetType())
        .swimmingPoolId(i.swimmingPoolId())
        .swimmingPoolName(i.swimmingPoolName())
        .date(i.date())
        .startTime(i.startTime())
        .endTime(i.endTime())
        .minTicketPrice(i.minTicketPrice())
        .isClosed(i.isClosed())
        .build();
  }

  private String imageUrl(@NonNull String imagePath) {
    return imageUrlPort.getImageUrl(imagePath);
  }

}
