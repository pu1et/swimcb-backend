package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.adapter.in.FindBoNoticeDetailResponse;
import com.project.swimcb.bo.notice.application.in.FindBoNoticeDetailUseCase;
import com.project.swimcb.db.repository.NoticeRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindBoNoticeDetailInteractor implements FindBoNoticeDetailUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public FindBoNoticeDetailResponse findDetail(long faqId) {
    val notice = noticeRepository.findById(faqId)
        .orElseThrow(() -> new NoSuchElementException("공지사항이 존재하지 않습니다."));
    return FindBoNoticeDetailResponse.from(notice);
  }
}
