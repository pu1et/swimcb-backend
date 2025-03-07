package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.faq.adapter.out.UploadedFile;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadNoticeFileResponse(
    @NonNull String name,
    @NonNull String path,
    @NonNull String url,
    long size
) {

  public static UploadNoticeFileResponse from(@NonNull UploadedFile file) {
    return UploadNoticeFileResponse.builder()
        .name(file.name())
        .path(file.path())
        .url(file.url())
        .size(file.size())
        .build();
  }
}
