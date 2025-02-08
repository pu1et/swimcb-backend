package com.project.swimcb.faq.adapter.in;

import com.project.swimcb.faq.domain.Faq;
import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindFaqsResponse(
    long faqId,
    @NonNull String title,
    @NonNull String createdBy,
    @NonNull LocalDate createdAt,
    boolean isVisible
) {

  public static FindFaqsResponse from(@NonNull Faq faq) {
    return FindFaqsResponse.builder()
        .faqId(faq.getId())
        .title(faq.getTitle())
        .createdBy(faq.getCreatedBy())
        .createdAt(faq.getCreatedAt().toLocalDate())
        .isVisible(faq.isVisible())
        .build();
  }
}
