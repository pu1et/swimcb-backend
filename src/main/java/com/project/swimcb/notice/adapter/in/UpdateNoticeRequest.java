package com.project.swimcb.notice.adapter.in;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateNoticeRequest(
    @NotNull(message = "title은 null이 아니어야 합니다.")
    String title,

    @NotNull(message = "content는 null이 아니어야 합니다.")
    String content,

    @NotNull(message = "imageUrls는 null이 아니어야 합니다.")
    List<String> imageUrls,

    boolean isVisible
) {

}
