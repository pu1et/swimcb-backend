package com.project.swimcb.faq.adapter.in;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindFaqsResponse(
    long faqId,
    @NonNull String title,
    @NonNull String createdBy,
    @NonNull LocalDate createdAt,
    boolean isVisible
) {

}
