package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.adapter.in.FindNoticeDetailResponse;

public interface FindNoticeDetailUseCase {

  FindNoticeDetailResponse findDetail(long faqId);
}
