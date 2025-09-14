package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.faq.adapter.out.UploadedFile;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadBoNoticeFileResponse(
    @NonNull String name,
    @NonNull String path,
    @NonNull String url,
    long size
) {

  public static UploadBoNoticeFileResponse from(@NonNull UploadedFile file) {
    return UploadBoNoticeFileResponse.builder()
        .name(file.name())
        .path(file.path())
        .url(file.url())
        .size(file.size())
        .build();
  }
}
