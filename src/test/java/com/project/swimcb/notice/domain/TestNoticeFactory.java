package com.project.swimcb.notice.domain;

import java.time.LocalDateTime;

public class TestNoticeFactory {

  public static Notice create(long id, String title, String content, boolean isVisible,
      LocalDateTime createdAt,
      String createdBy, LocalDateTime updatedAt, String updatedBy) {
    return Notice.test()
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

  public static Notice create(long id, String title, String content, boolean isVisible) {
    return Notice.test()
        .id(id)
        .title(title)
        .content(content)
        .isVisible(isVisible)
        .build();
  }
}