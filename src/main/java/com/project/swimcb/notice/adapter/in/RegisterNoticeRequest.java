package com.project.swimcb.notice.adapter.in;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record RegisterNoticeRequest(
    @NotNull(message = "createdBy는 null일 수 없습니다.")
    String createdBy,

    @NotNull(message = "title은 null일 수 없습니다.")
    String title,

    @NotNull(message = "content는 null일 수 없습니다.")
    String content,

    @NotNull(message = "imageUrls는 null일 수 없습니다.")
    List<String> imageUrls,

    boolean isVisible
) {

}
