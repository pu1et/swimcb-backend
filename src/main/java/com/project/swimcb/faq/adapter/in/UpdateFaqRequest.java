package com.project.swimcb.faq.adapter.in;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateFaqRequest(
    @NotNull(message = "FAQ 제목은 null이 아니어야 합니다.")
    String title,

    @NotNull(message = "FAQ 내용은 null이 아니어야 합니다.")
    String content,

    @NotNull(message = "FAQ 이미지 URL 리스트는 null이 아니어야 합니다.")
    List<String> imageUrls,

    boolean isVisible
) {


}
