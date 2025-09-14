package com.project.swimcb.bo.notice.adapter.in;

import java.util.List;
import lombok.NonNull;

public record DeleteBoNoticeRequest(
    @NonNull List<Long> noticeIds
) {

}
