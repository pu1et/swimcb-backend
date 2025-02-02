package com.project.swimcb.faq.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FaqSummary(
    long faqId,
    @NonNull String title,
    @NonNull String createdBy,
    @NonNull LocalDate createdAt,
    boolean isVisible
) {

}
