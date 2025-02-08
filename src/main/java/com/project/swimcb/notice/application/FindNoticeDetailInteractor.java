package com.project.swimcb.notice.application;

import com.project.swimcb.notice.adapter.in.FindNoticeDetailResponse;
import com.project.swimcb.notice.application.in.FindNoticeDetailUseCase;
import com.project.swimcb.notice.application.out.ImageUrlPrefixProvider;
import com.project.swimcb.notice.domain.NoticeImageRepository;
import com.project.swimcb.notice.domain.NoticeRepository;
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
  private final NoticeImageRepository noticeImageRepository;
  private final ImageUrlPrefixProvider imageUrlPrefixProvider;

  @Override
  public FindNoticeDetailResponse findDetail(long faqId) {
    val notice = noticeRepository.findById(faqId)
        .orElseThrow(() -> new NoSuchElementException("공지사항이 존재하지 않습니다."));

    val noticeImages = noticeImageRepository.findByNoticeId(notice.getId())
        .stream().map(i -> imageUrlPrefixProvider.provide() + i.getPath()).toList();

    return FindNoticeDetailResponse.from(notice, noticeImages);
  }
}
