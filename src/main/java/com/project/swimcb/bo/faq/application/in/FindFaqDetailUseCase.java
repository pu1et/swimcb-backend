package com.project.swimcb.bo.faq.application.in;

import com.project.swimcb.bo.faq.adapter.in.FindFaqDetailResponse;

public interface FindFaqDetailUseCase {

  FindFaqDetailResponse findDetail(long faqId);
}
