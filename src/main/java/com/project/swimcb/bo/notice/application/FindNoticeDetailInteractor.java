package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.adapter.in.FindNoticeDetailResponse;
import com.project.swimcb.bo.notice.application.in.FindNoticeDetailUseCase;
import com.project.swimcb.db.repository.NoticeRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindNoticeDetailInteractor implements FindNoticeDetailUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public FindNoticeDetailResponse findDetail(long faqId) {
    val notice = noticeRepository.findById(faqId)
        .orElseThrow(() -> new NoSuchElementException("공지사항이 존재하지 않습니다."));
    return FindNoticeDetailResponse.from(notice);
  }
}
