package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.adapter.in.FindBoNoticeDetailResponse;

public interface FindBoNoticeDetailUseCase {

  FindBoNoticeDetailResponse findDetail(long faqId);
}
