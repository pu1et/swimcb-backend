package com.project.swimcb.notice.adapter.in;

import com.project.swimcb.notice.domain.Notice;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindNoticeDetailResponse(
    @NonNull String createdBy,
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imageUrls,
    boolean isVisible,
    @NonNull LocalDate createdAt
) {

  public static FindNoticeDetailResponse from(@NonNull Notice notice,
      @NonNull List<String> imageUrls) {
    return FindNoticeDetailResponse.builder()
        .createdBy(notice.getCreatedBy())
        .title(notice.getTitle())
        .content(notice.getContent())
        .imageUrls(imageUrls)
        .isVisible(notice.isVisible())
        .createdAt(notice.getCreatedAt().toLocalDate())
        .build();
  }
}
