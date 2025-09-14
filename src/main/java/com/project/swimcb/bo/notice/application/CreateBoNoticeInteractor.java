package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.application.in.CreateBoNoticeUseCase;
import com.project.swimcb.bo.notice.domain.CreateBoNoticeCommand;
import com.project.swimcb.db.entity.NoticeEntity;
import com.project.swimcb.db.repository.NoticeRepository;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateBoNoticeInteractor implements CreateBoNoticeUseCase {

  private final SwimmingPoolRepository swimmingPoolRepository;
  private final NoticeRepository noticeRepository;

  @Override
  public void createNotice(@NonNull CreateBoNoticeCommand command) {

    val swimmingPool = swimmingPoolRepository.findById(command.swimmingPoolId())
        .orElseThrow(
            () -> new NoSuchElementException("수영장이 존재하지 않습니다 : " + command.swimmingPoolId()));

    val notice = NoticeEntity.create(
        command.title(),
        command.content(),
        command.isVisible(),
        swimmingPool
    );
    noticeRepository.save(notice);
  }

}
