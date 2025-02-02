package com.project.swimcb.notice.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record NoticeSummary(
    long noticeId,
    @NonNull String title,
    @NonNull String createdBy,
    @NonNull LocalDate createdAt,
    boolean isVisible
) {

}
