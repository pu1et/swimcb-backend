package com.project.swimcb.bo.notice.adapter.in;

import java.util.List;
import lombok.NonNull;

public record DeleteNoticeRequest(
    @NonNull List<Long> noticeIds
) {

}
