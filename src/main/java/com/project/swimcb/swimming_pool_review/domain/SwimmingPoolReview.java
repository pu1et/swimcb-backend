package com.project.swimcb.swimming_pool_review.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.bo.swimmingpool.domain.SwimmingPool;
import com.project.swimcb.common.entity.BaseEntity;
import com.project.swimcb.member.domain.Member;
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
public class SwimmingPoolReview extends BaseEntity {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPool swimmingPool;

  @Lob
  @Column(name = "review", nullable = false)
  private String review;
}
