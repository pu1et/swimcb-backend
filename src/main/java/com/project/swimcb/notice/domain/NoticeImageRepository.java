package com.project.swimcb.notice.domain;

import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeImageRepository extends JpaRepository<NoticeImage, Long> {

  List<NoticeImage> findByNoticeId(@NonNull Long noticeId);

  void deleteByNoticeId(@NonNull Long noticeId);
}
