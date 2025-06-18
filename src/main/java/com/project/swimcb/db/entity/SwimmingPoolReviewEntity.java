package com.project.swimcb.db.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Table(name = "swimming_pool_review")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SwimmingPoolReviewEntity extends BaseEntity {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity member;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPoolEntity swimmingPool;

  @Lob
  @Column(name = "review", nullable = false)
  private String review;
}
