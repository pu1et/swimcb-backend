package com.project.swimcb.notice.adapter.in;

import java.util.List;
import lombok.NonNull;

public record UpdateNoticeRequest(
    @NonNull String title,
    @NonNull String content,
    @NonNull List<String> imageUrls,
    boolean isVisible
) {

}
