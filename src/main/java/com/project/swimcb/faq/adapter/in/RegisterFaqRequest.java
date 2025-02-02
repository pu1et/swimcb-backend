package com.project.swimcb.faq.adapter.in;

import java.util.List;
import lombok.NonNull;

public record RegisterFaqRequest(
    @NonNull String createdBy,
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imageUrls,
    boolean isVisible
) {

}
