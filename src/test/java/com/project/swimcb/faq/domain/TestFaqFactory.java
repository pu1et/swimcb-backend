package com.project.swimcb.faq.domain;

import java.time.LocalDateTime;

public class TestFaqFactory {

  public static Faq create(long id, String title, String content, boolean isVisible,
      LocalDateTime createdAt,
      String createdBy, LocalDateTime updatedAt, String updatedBy) {
    return Faq.test()
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