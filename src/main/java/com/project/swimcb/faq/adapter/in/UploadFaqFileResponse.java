package com.project.swimcb.faq.adapter.in;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadFaqFileResponse(
    @NonNull String name,
    @NonNull String path,
    long size
) {

}
