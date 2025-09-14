package com.project.swimcb.bo.notice.domain;

import com.project.swimcb.bo.notice.adapter.in.CreateBoNoticeRequest;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateBoNoticeCommand(
    @NonNull String createdBy,
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imagePaths,
    boolean isVisible
) {

  public static CreateBoNoticeCommand from(@NonNull CreateBoNoticeRequest request) {
    return CreateBoNoticeCommand.builder()
        .createdBy(request.createdBy())
        .title(request.title())
        .content(request.content())
        .imagePaths(request.imagePaths())
        .isVisible(request.isVisible())
        .build();
  }
}
