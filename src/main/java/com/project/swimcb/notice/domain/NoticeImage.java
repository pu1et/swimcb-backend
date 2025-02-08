package com.project.swimcb.notice.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "notice_image")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class NoticeImage extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "notice_id", nullable = false)
  private Notice notice;

  @Column(name = "path", nullable = false)
  private String path;

  @Builder(access = PACKAGE, builderMethodName = "test", builderClassName = "test")
  private NoticeImage(long id, long noticeId, String path, LocalDateTime createdAt,
      String createdBy, LocalDateTime updatedAt, String updatedBy) {
    super(createdAt, createdBy, updatedAt, updatedBy);
    this.id = id;
    this.notice = Notice.test().id(noticeId).build();
    this.path = path;
  }
}
