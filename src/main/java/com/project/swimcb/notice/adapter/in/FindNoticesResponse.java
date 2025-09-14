package com.project.swimcb.notice.adapter.in;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public record FindNoticesResponse(
    @NonNull Page<NoticeResponse> notices
) {

  @Builder
  public record NoticeResponse(
      @NonNull Long noticeId,
      @NonNull String title,
      @NonNull String content,
      @NonNull LocalDate date
  ) {

  }

}
