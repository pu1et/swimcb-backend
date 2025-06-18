package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.db.entity.FaqEntity;
import java.util.List;
import lombok.NonNull;

public record CreateFaqRequest(
    @NonNull String createdBy,
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imagePaths,
    boolean isVisible
) {

  public FaqEntity toDomain() {
    return FaqEntity.builder()
        .title(this.title)
        .content(this.content)
        .isVisible(this.isVisible)
        .build();
  }
}
