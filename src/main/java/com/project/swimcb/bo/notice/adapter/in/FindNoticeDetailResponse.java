package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.domain.Notice;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindNoticeDetailResponse(
    @NonNull String createdBy,
    @NonNull String title,
    @NonNull String content,
    boolean isVisible,
    @NonNull LocalDate createdAt
) {

  public static FindNoticeDetailResponse from(@NonNull Notice notice) {
    return FindNoticeDetailResponse.builder()
        .createdBy(notice.getCreatedBy())
        .title(notice.getTitle())
        .content(notice.getContent())
        .isVisible(notice.isVisible())
        .createdAt(notice.getCreatedAt().toLocalDate())
        .build();
  }
}
