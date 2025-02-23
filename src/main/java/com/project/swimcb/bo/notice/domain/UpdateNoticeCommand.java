package com.project.swimcb.bo.notice.domain;

import com.project.swimcb.bo.notice.adapter.in.UpdateNoticeRequest;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateNoticeCommand(
    long noticeId,
    @NonNull String title,
    @NonNull String content,
    boolean isVisible
) {

  public static UpdateNoticeCommand from(long noticeId, @NonNull UpdateNoticeRequest request) {
    return UpdateNoticeCommand.builder()
        .noticeId(noticeId)
        .title(request.title())
        .content(request.content())
        .isVisible(request.isVisible())
        .build();
  }
}
