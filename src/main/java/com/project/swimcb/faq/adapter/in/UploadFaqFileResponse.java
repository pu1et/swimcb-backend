package com.project.swimcb.faq.adapter.in;

import com.project.swimcb.faq.adapter.out.UploadedFile;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadFaqFileResponse(
    @NonNull String name,
    @NonNull String path,
    long size
) {

  public static UploadFaqFileResponse from(@NonNull UploadedFile uploadedFile) {
    return UploadFaqFileResponse.builder()
        .name(uploadedFile.name())
        .path(uploadedFile.path())
        .size(uploadedFile.size())
        .build();
  }
}
