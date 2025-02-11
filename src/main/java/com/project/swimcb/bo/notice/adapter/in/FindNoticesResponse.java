package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.domain.Notice;
import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindNoticesResponse(
    long noticeId,
    @NonNull String title,
    @NonNull String createdBy,
    @NonNull LocalDate createdAt,
    boolean isVisible
) {

  public static FindNoticesResponse from(@NonNull Notice notice) {
    return FindNoticesResponse.builder()
        .noticeId(notice.getId())
        .title(notice.getTitle())
        .createdBy(notice.getCreatedBy())
        .createdAt(notice.getCreatedAt().toLocalDate())
        .isVisible(notice.isVisible())
        .build();
  }
}
