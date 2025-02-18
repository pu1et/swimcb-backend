package com.project.swimcb.bo.notice.adapter.in;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateNoticeRequest(
    @NotNull(message = "createdBy는 null일 수 없습니다.")
    String createdBy,

    @NotNull(message = "title은 null일 수 없습니다.")
    String title,

    @NotNull(message = "content는 null일 수 없습니다.")
    String content,

    @NotNull(message = "imagePaths는 null일 수 없습니다.")
    List<String> imagePaths,

    boolean isVisible
) {

}
