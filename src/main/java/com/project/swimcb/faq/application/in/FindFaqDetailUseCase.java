package com.project.swimcb.faq.application.in;

import com.project.swimcb.faq.adapter.in.FindFaqDetailResponse;

public interface FindFaqDetailUseCase {

  FindFaqDetailResponse findDetail(long faqId);
}
