package com.project.swimcb.notice.application.in;

import com.project.swimcb.notice.adapter.in.FindNoticeDetailResponse;

public interface FindNoticeDetailUseCase {

  FindNoticeDetailResponse findDetail(long faqId);
}
