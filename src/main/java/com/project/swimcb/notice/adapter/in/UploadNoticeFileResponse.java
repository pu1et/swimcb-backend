package com.project.swimcb.notice.adapter.in;

import com.project.swimcb.faq.adapter.out.UploadedFile;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadNoticeFileResponse(
    @NonNull String name,
    @NonNull String url,
    long size
) {

  public static UploadNoticeFileResponse from(@NonNull UploadedFile file) {
    return UploadNoticeFileResponse.builder()
        .name(file.name())
        .url(file.url())
        .size(file.size())
        .build();
  }
}
