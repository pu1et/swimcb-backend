package com.project.swimcb.notice.adapter.in;

import com.project.swimcb.faq.adapter.out.UploadedFile;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadNoticeFileResponse(
    @NonNull String name,
    @NonNull String path,
    long size
) {

  public static UploadNoticeFileResponse from(@NonNull UploadedFile file) {
    return UploadNoticeFileResponse.builder()
        .name(file.name())
        .path(file.path())
        .size(file.size())
        .build();
  }
}
