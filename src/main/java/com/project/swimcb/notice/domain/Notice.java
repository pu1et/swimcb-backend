package com.project.swimcb.notice.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Notice(
    @NonNull Long noticeId,
    @NonNull String title,
    @NonNull String content,
    @NonNull LocalDate date
) {

}
