package com.project.swimcb.db.entity;

import java.time.LocalDateTime;

public class TestFaqFactory {

  public static FaqEntity create(long id, String title, String content, boolean isVisible,
      LocalDateTime createdAt,
      String createdBy, LocalDateTime updatedAt, String updatedBy) {
    return FaqEntity.test()
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
}
