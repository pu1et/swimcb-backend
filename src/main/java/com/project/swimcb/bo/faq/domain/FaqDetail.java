package com.project.swimcb.bo.faq.domain;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FaqDetail(
    @NonNull String createdBy,
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imageUrls,
    boolean isVisible,
    @NonNull LocalDate createdAt
) {

}
