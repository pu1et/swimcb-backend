package com.project.swimcb.faq.adapter.in;

import com.project.swimcb.faq.domain.Faq;
import java.util.List;
import lombok.NonNull;

public record RegisterFaqRequest(
    @NonNull String createdBy,
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imagePaths,
    boolean isVisible
) {

  public Faq toDomain() {
    return Faq.builder()
        .title(this.title)
        .content(this.content)
        .isVisible(this.isVisible)
        .build();
  }
}
