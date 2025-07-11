package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.swimmingpool.domain.FindFreeSwimmingResponse;
import com.project.swimcb.swimmingpool.domain.FreeSwimming;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindFreeSwimmingResponseMapper {

  private final ImageUrlPort imageUrlPort;

  public FindFreeSwimmingResponse toResponse(@NonNull List<FreeSwimming> freeSwimming) {
    val result = freeSwimming
        .stream()
        .map(i -> FindFreeSwimmingResponse.FreeSwimming.builder()
            .swimmingPoolId(i.swimmingPoolId())
            .imageUrl(getImageUrl(i.imagePath()))
            .isFavorite(i.isFavorite())
            .distance(i.distance())
            .name(i.name())
            .address(i.address())
            .rating(i.rating())
            .reviewCount(i.reviewCount())
            .build())
        .toList();
    return new FindFreeSwimmingResponse(result);
  }

  private String getImageUrl(String imagePath) {
    return imageUrlPort.getImageUrl(imagePath);
  }

}
