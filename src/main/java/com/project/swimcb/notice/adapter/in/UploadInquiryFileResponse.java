package com.project.swimcb.notice.adapter.in;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadInquiryFileResponse(
    @NonNull String name,
    @NonNull String path,
    long size
) {

}
