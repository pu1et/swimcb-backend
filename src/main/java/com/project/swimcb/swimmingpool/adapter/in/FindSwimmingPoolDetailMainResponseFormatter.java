package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindSwimmingPoolDetailMainResponseFormatter implements
    FindSwimmingPoolDetailMainResponseFactory {

  private final ImageUrlPort imageUrlPort;

  @Override
  public FindSwimmingPoolDetailMainResponse create(@NonNull SwimmingPoolDetailMain pool) {
    val imageUrls = pool.imagePaths().stream().map(imageUrlPort::getImageUrl).toList();

    return FindSwimmingPoolDetailMainResponse.builder()
        .imageUrls(imageUrls)
        .name(pool.name())
        .favoriteId(pool.favoriteId())
        .rating(pool.rating())
        .reviewCount(pool.reviewCount())
        .address(pool.address())
        .phone(pool.phone())
        .latitude(pool.latitude())
        .longitude(pool.longitude())
        .build();
  }

}
