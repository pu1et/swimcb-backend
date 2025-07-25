package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.db.entity.FaqEntity;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindFaqDetailResponse(
    @NonNull String title,
    @NonNull String content,
    boolean isVisible
) {


  public static FindFaqDetailResponse from(@NonNull FaqEntity faq) {
    return FindFaqDetailResponse.builder()
        .title(faq.getTitle())
        .content(faq.getContent())
        .isVisible(faq.isVisible())
        .build();
  }
}
