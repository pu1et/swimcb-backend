package com.project.swimcb.notice.domain;

import com.project.swimcb.notice.adapter.in.UpdateNoticeRequest;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateNoticeCommand(
    long noticeId,
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imageUrls,
    boolean isVisible
) {

  public static UpdateNoticeCommand from(long noticeId, @NonNull UpdateNoticeRequest request) {
    return UpdateNoticeCommand.builder()
        .noticeId(noticeId)
        .title(request.title())
        .content(request.content())
        .imageUrls(request.imageUrls())
        .isVisible(request.isVisible())
        .build();
  }
}
