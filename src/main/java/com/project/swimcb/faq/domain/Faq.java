package com.project.swimcb.faq.domain;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

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
import lombok.NonNull;
import lombok.ToString;

@ToString
@Getter
@Table(name = "faq")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Faq extends BaseEntity {

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

  @Builder(access = PUBLIC)
  private Faq(String title, String content, boolean isVisible) {
    this.title = title;
    this.content = content;
    this.isVisible = isVisible;
  }

  @Builder(access = PACKAGE, builderMethodName = "test", builderClassName = "test")
  private Faq(long id, String title, String content, boolean isVisible, LocalDateTime createdAt,
      String createdBy, LocalDateTime updatedAt, String updatedBy) {
    super(createdAt, createdBy, updatedAt, updatedBy);
    this.id = id;
    this.title = title;
    this.content = content;
    this.isVisible = isVisible;
  }

  public void update(@NonNull String title, @NonNull String content, boolean visible) {
    this.title = title;
    this.content = content;
    this.isVisible = visible;
  }

  public void updateIsVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }
}
