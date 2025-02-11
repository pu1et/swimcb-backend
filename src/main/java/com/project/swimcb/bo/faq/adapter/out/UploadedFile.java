package com.project.swimcb.bo.faq.adapter.out;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UploadedFile(
    @NonNull String name,
    @NonNull String path,
    @NonNull String url,
    long size
) {

}
