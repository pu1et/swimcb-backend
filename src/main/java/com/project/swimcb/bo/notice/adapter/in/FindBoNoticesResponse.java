package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.db.entity.NoticeEntity;
import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoNoticesResponse(
    long noticeId,
    @NonNull String title,
    @NonNull String createdBy,
    @NonNull LocalDate createdAt,
    boolean isVisible
) {

  public static FindBoNoticesResponse from(@NonNull NoticeEntity notice) {
    return FindBoNoticesResponse.builder()
        .noticeId(notice.getId())
        .title(notice.getTitle())
        .createdBy(notice.getCreatedBy())
        .createdAt(notice.getCreatedAt().toLocalDate())
        .isVisible(notice.isVisible())
        .build();
  }
}
