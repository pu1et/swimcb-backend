package com.project.swimcb.bo.notice.domain;

import com.project.swimcb.bo.notice.domain.NoticeImage;
import java.time.LocalDateTime;

public class TestNoticeImageFactory {

  public static NoticeImage create(long id, long noticeId, String path, LocalDateTime createdAt,
      String createdBy, LocalDateTime updatedAt, String updatedBy) {
    return NoticeImage.test()
        .id(id)
        .noticeId(noticeId)
        .path(path)
        .createdAt(createdAt)
        .createdBy(createdBy)
        .updatedAt(updatedAt)
        .updatedBy(updatedBy)
        .build();
  }
}