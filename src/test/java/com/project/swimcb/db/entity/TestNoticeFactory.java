package com.project.swimcb.db.entity;

import java.time.LocalDateTime;

public class TestNoticeFactory {

  public static NoticeEntity create(long id, String title, String content, boolean isVisible,
      LocalDateTime createdAt,
      String createdBy, LocalDateTime updatedAt, String updatedBy) {
    return NoticeEntity.test()
        .id(id)
        .title(title)
        .content(content)
        .isVisible(isVisible)
        .createdAt(createdAt)
        .createdBy(createdBy)
        .updatedAt(updatedAt)
        .updatedBy(updatedBy)
        .build();
  }

  public static NoticeEntity create(long id, String title, String content, boolean isVisible) {
    return NoticeEntity.test()
        .id(id)
        .title(title)
        .content(content)
        .isVisible(isVisible)
        .build();
  }
}
