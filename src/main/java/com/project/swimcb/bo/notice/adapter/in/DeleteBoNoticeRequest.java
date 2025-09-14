package com.project.swimcb.bo.notice.adapter.in;

import jakarta.validation.constraints.Size;
import java.util.List;

public record DeleteBoNoticeRequest(

    @Size(min = 1, message = "noticeIds는 1개 이상이어야 합니다")
    List<Long> noticeIds
) {

}
