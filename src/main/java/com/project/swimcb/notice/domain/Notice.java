package com.project.swimcb.notice.domain;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Table(name = "notice")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Notice extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "is_visible", nullable = false)
  private boolean isVisible;

  @Builder(access = PACKAGE, builderMethodName = "test", builderClassName = "test")
  private Notice(long id, String title, String content, boolean isVisible, LocalDateTime createdAt,
      String createdBy, LocalDateTime updatedAt, String updatedBy) {
    super(createdAt, createdBy, updatedAt, updatedBy);
    this.id = id;
    this.title = title;
    this.content = content;
    this.isVisible = isVisible;
  }
}
