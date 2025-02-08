package com.project.swimcb.faq.domain;

import com.project.swimcb.faq.adapter.in.UpdateFaqRequest;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateFaqCommand(
    long faqId,
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imageUrls,
    boolean isVisible
) {

  public static UpdateFaqCommand from(long faqId, @NonNull UpdateFaqRequest request) {
    return UpdateFaqCommand.builder()
        .faqId(faqId)
        .title(request.title())
        .content(request.content())
        .imageUrls(request.imageUrls())
        .isVisible(request.isVisible())
        .build();
  }
}
