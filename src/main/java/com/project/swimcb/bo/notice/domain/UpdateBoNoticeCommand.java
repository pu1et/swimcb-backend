package com.project.swimcb.bo.notice.domain;

import com.project.swimcb.bo.notice.adapter.in.UpdateBoNoticeRequest;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateBoNoticeCommand(
    long noticeId,
    @NonNull String title,
    @NonNull String content,
    boolean isVisible
) {

  public static UpdateBoNoticeCommand from(long noticeId, @NonNull UpdateBoNoticeRequest request) {
    return UpdateBoNoticeCommand.builder()
        .noticeId(noticeId)
        .title(request.title())
        .content(request.content())
        .isVisible(request.isVisible())
        .build();
  }
}
