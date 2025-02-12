package com.project.swimcb.bo.swimmingpool.adapter.in;

import com.project.swimcb.bo.faq.adapter.out.UploadedFile;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadSwimmingPoolUsageAgreementResponse(
    @NonNull String name,
    @NonNull String path,
    @NonNull String url,
    long size
) {

  public static UploadSwimmingPoolUsageAgreementResponse from(@NonNull UploadedFile file) {
    return UploadSwimmingPoolUsageAgreementResponse.builder()
        .name(file.name())
        .path(file.path())
        .url(file.url())
        .size(file.size())
        .build();
  }
}
